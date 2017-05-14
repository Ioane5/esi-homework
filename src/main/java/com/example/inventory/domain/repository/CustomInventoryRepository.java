package com.example.inventory.domain.repository;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;

import java.util.List;

public interface CustomInventoryRepository {
    /**
     * Find PlantInventoryItems that are available (not rented)
     * after certain date in the past
     * @param period business period of interest
     * @return list of PlantInventoryItem
     */
    List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(String entryId, BusinessPeriod period);
    List<PlantInventoryItem> findPlantItemsNotHiredInLastSixMonths();
    List<PlantInventoryEntry> findAvailablePlants(String name, BusinessPeriod period);
}