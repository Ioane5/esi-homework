package com.example.sales.application.services;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    PurchaseOrderRepository orderRepo;

    public PurchaseOrder createPO(PlantInventoryEntry plant, BusinessPeriod period) {
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextId(), plant, LocalDate.now(), period);
        orderRepo.save(po);

        try {
            PlantReservation pr = inventoryService.reservePlantItem(plant, period, po);
            po.addReservationAndOpenPO(pr);
        } catch (PlantNotFoundException e) {
            po.rejectPO();
        }
        orderRepo.save(po);
        return po;
    }

    public PurchaseOrder findPO(String id) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = orderRepo.findOne(id);
        if(po==null){
            throw new PurchaseOrderNotFoundException();
        }

        return po;
    }

    public List<PurchaseOrder> findPOs() {
        return orderRepo.findAll();
    }
}
