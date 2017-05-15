package com.example.sales.application.services;

import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
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
import java.util.List;

@Service
public class SalesService {
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
}
