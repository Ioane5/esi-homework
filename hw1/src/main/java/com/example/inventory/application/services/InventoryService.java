package com.example.inventory.application.services;

import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepo;
    @Autowired
    PlantReservationRepository plantReservationRepository;

    public PlantReservation reservePlantItem(PlantInventoryEntry entry, BusinessPeriod period, PurchaseOrder po) throws NoPlantAvailableException {
        List<PlantInventoryItem> items = inventoryRepo.findAvailablePlantsInBusinessPeriod(entry.getId(), period);
        if (items.size() < 1) {
            throw new NoPlantAvailableException();
        }
        PlantInventoryItem freePlant = items.get(0);

        PlantReservation pr = PlantReservation.of(IdentifierFactory.nextId(), period, freePlant).withPurchaseOrder(po);
        plantReservationRepository.save(pr);

        return pr;
    }

    public static class NoPlantAvailableException extends Exception {

    }

    public List<PlantInventoryEntry> findAvailablePlants(String name, BusinessPeriod businessPeriod) {
        return inventoryRepo.findAvailablePlants(name, businessPeriod);
    }
}
