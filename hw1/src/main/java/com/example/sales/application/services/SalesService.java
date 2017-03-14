package com.example.sales.application.services;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.domain.validation.PurchaseOrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.time.LocalDate;

@Service
public class SalesService {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PurchaseOrderRepository orderRepo;

    @Autowired
    PurchaseOrderValidator poValidator;

    public PurchaseOrder createPO(PlantInventoryEntry plant, BusinessPeriod period) {
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextId(), plant, LocalDate.now(), period);
        DataBinder binder = new DataBinder(po);
        binder.addValidators(poValidator);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) {
            // TODO: what should happen here?
            // throw POValidationException?!
        } else {
            orderRepo.save(po);
        }

        try {
            PlantReservation pr = inventoryService.reservePlantItem(plant, period, po);
            po.addReservationAndOpenPO(pr);
        } catch (PlantNotFoundException e) {
            po.rejectPO();
        }
        orderRepo.save(po);
        return po;
    }
}
