package com.example.sales.application.services;

import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.sales.domain.model.Customer;
import com.example.sales.domain.model.Invoice;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.domain.validation.PurchaseOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class SalesService {
    @Autowired
    private PlantReservationRepository plantReservationRepo;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PurchaseOrderRepository orderRepo;
    @Autowired
    private PurchaseOrderValidator poValidator;
    @Autowired
    private InvoiceService invoiceService;

    public PurchaseOrder createPO(Customer customer, String plantId, BusinessPeriod period) throws POValidationException {
        PlantInventoryEntry plant = inventoryService.findPlant(plantId);
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextId(), customer, plant, LocalDate.now(), period);
        validateAndSavePO(po);

        try {
            PlantReservation pr = inventoryService.reservePlantItem(plant, period, po);
            po.addReservationAndAcceptPO(pr);
        } catch (PlantNotFoundException e) {
            po.reject();
        } finally {
            validateAndSavePO(po);
        }
        return po;
    }

    public PurchaseOrder findPO(String id, Customer customer) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = orderRepo.findByIdAndCustomer(id, customer);
        if (po == null) {
            throw new PurchaseOrderNotFoundException();
        }

        return po;
    }

    private PurchaseOrder findPO(String id) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = orderRepo.findOne(id);
        if (po == null) {
            throw new PurchaseOrderNotFoundException();
        }

        return po;
    }

    public PurchaseOrder cancelPurchaseOrder(String id) throws PurchaseOrderNotFoundException, POValidationException {
        PurchaseOrder po = findPO(id);
        List<POStatus> acceptedStatuses = Arrays.asList(POStatus.PENDING, POStatus.ACCEPTED);
        if (acceptedStatuses.contains(po.getStatus())) {
            po.cancel();
            orderRepo.save(po);
            return po;
        } else {
            throw new POValidationException("Purchase order is already dispatched");
        }
    }

    public List<PurchaseOrder> findAllPOs(Customer customer) {
        return orderRepo.findAllByCustomer(customer);
    }

    public List<PurchaseOrder> findDispatches(LocalDate date) {
        return orderRepo.findDispatches(date);
    }

    public List<PurchaseOrder> findArrivals(LocalDate date) {
        return orderRepo.findArrivals(date);
    }

    private void validateAndSavePO(PurchaseOrder po) throws POValidationException {
        DataBinder binder = new DataBinder(po);
        binder.addValidators(poValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            throw new POValidationException(binder.getBindingResult().getAllErrors().toString());
        } else {
            orderRepo.save(po);
        }
    }

    public PurchaseOrder dispatchPO(String id) throws PurchaseOrderNotFoundException, POValidationException {
        PurchaseOrder po = findPO(id);
        if (po.getStatus() == POStatus.ACCEPTED) {
            po.dispatch();
            return orderRepo.save(po);
        } else {
            throw new POValidationException("Purchase order can not be dispatched");
        }
    }

    public PurchaseOrder acceptDelivery(String id) throws PurchaseOrderNotFoundException, POValidationException {
        PurchaseOrder po = findPO(id);
        if (po.getStatus() == POStatus.PLANT_DISPATCHED) {
            po.acceptDelivery();
            return orderRepo.save(po);
        } else {
            throw new POValidationException("Purchase order is not dispatched yet");
        }
    }

    public PurchaseOrder rejectDelivery(String id) throws PurchaseOrderNotFoundException, POValidationException {
        PurchaseOrder po = findPO(id);
        if (po.getStatus() == POStatus.PLANT_DISPATCHED) {
            po.rejectDelivery();
            return orderRepo.save(po);
        } else {
            throw new POValidationException("Purchase order is not dispatched yet");
        }
    }

    public PurchaseOrder returnPlant(String id) throws PurchaseOrderNotFoundException, POValidationException {
        PurchaseOrder po = findPO(id);
        if (po.getStatus() == POStatus.PLANT_DELIVERED) {
            po.returnPlant();
            orderRepo.save(po);

            Invoice invoice = invoiceService.createInvoice(po);
            try {
                invoiceService.sendInvoice(invoice, po.getCustomer().getEmail());
            } catch (Exception e) {
                System.err.println("Invoice has not been sent");
                e.printStackTrace();
            }
            return po;
        } else {
            throw new POValidationException("Purchase order is not delivered yet");
        }
    }

    public PurchaseOrder resubmitPO(String id, BusinessPeriod newPeriod) throws PurchaseOrderNotFoundException, POValidationException {
        PurchaseOrder purchaseOrder = findPO(id);

        if (!isPOResubmissionEnabled(purchaseOrder, newPeriod)) {
            throw new POValidationException("Resubmission not permitted");
        }

        if (inventoryService.canChangeReservationPeriod(purchaseOrder.getReservation(), newPeriod)) {
            purchaseOrder.updateRentalPeriod(newPeriod);
            purchaseOrder.getReservation().setSchedule(newPeriod);
            // If plant is Rejected OR Pending it should get accepted, if it is Delivered, it stays.
            if (purchaseOrder.getStatus() == POStatus.PENDING || purchaseOrder.getStatus() == POStatus.REJECTED) {
                purchaseOrder.accept();
            }
            plantReservationRepo.save(purchaseOrder.getReservation());
            validateAndSavePO(purchaseOrder);
        } else {
            throw new POValidationException("Resubmission is not available, plant is busy");
        }
        return purchaseOrder;
    }

    private boolean isPOResubmissionEnabled(PurchaseOrder purchaseOrder, BusinessPeriod requestedPeriod) {
        BusinessPeriod currentPeriod = purchaseOrder.getRentalPeriod();

        boolean startDateChanged = !currentPeriod.getStartDate().isEqual(requestedPeriod.getStartDate());
        boolean endDateChanged = !currentPeriod.getEndDate().isEqual(requestedPeriod.getEndDate());

        List<POStatus> acceptedStatuses;
        if (startDateChanged && endDateChanged) {
            acceptedStatuses = Arrays.asList(POStatus.PENDING, POStatus.ACCEPTED, POStatus.REJECTED);
            // If start date is changed, it should be after present time :)
            if (requestedPeriod.getStartDate().isAfter(LocalDate.now())) {
                return false;
            }
        } else if (!startDateChanged && endDateChanged) {
            acceptedStatuses = Collections.singletonList(POStatus.PLANT_DELIVERED);
        } else {
            return false;
        }
        return acceptedStatuses.contains(purchaseOrder.getStatus());
    }
}
