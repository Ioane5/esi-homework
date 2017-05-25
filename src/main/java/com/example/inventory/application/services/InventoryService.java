package com.example.inventory.application.services;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
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
    private PlantReservationRepository reservationRepo;
    @Autowired
    private PlantInventoryItemRepository itemRepo;

    public PlantReservation reservePlantItem(PlantInventoryEntry entry, BusinessPeriod period, PurchaseOrder po) throws PlantNotFoundException {
        List<PlantInventoryItem> items = inventoryRepo.findAvailablePlantItemsInBusinessPeriod(entry.getId(), period);
        if (items.size() < 1) {
            throw new PlantNotFoundException("The requested plant is unavailable");
        }
        PlantInventoryItem freePlant = items.get(0);

        PlantReservation reservation = PlantReservation.of(IdentifierFactory.nextId(), period, freePlant).withPurchaseOrder(po);
        return reservationRepo.save(reservation);
    }

    public PlantReservation reservePlantItem(String itemId, BusinessPeriod period, String maintenancePlanId) throws PlantNotFoundException {
        PlantInventoryItem item = itemRepo.findOne(itemId);
        if (item == null) {
            throw new PlantNotFoundException("Item with ID: " + itemId + ", does not exist");
        }
        PlantReservation reservation = PlantReservation.of(IdentifierFactory.nextId(), period, item)
                .withMaintenancePlanId(maintenancePlanId);
        return reservationRepo.save(reservation);
    }

    public List<PlantInventoryEntry> findAvailablePlants(String name, BusinessPeriod businessPeriod) {
        return inventoryRepo.findAvailablePlants(name, businessPeriod);
    }

    public PlantInventoryEntry findPlant(String plantId) {
        return inventoryRepo.findOne(plantId);
    }

    public boolean canChangeReservationPeriod(PlantReservation reservation, BusinessPeriod newPeriod) {
        return inventoryRepo.canChangeReservationPeriod(reservation, newPeriod);
    }
}
