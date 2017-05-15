package com.example.inventory.application.services;

import com.example.common.application.exceptions.PlantNotFoundException;
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
    private PlantReservationRepository plantReservationRepository;

    public PlantReservation reservePlantItem(PlantInventoryEntry entry, BusinessPeriod period, PurchaseOrder po) throws PlantNotFoundException {
        List<PlantInventoryItem> items = inventoryRepo.findAvailablePlantItemsInBusinessPeriod(entry.getId(), period);
        if (items.size() < 1) {
            throw new PlantNotFoundException("The requested plant is unavailable");
        }
        PlantInventoryItem freePlant = items.get(0);

        PlantReservation pr = PlantReservation.of(IdentifierFactory.nextId(), period, freePlant).withPurchaseOrder(po);
        return plantReservationRepository.save(pr);
    }

    public List<PlantInventoryEntry> findAvailablePlants(String name, BusinessPeriod businessPeriod) {
        return inventoryRepo.findAvailablePlants(name, businessPeriod);
    }

    public PlantInventoryEntry findPlant(String plantId) {
        return inventoryRepo.findOne(plantId);
    }
}
