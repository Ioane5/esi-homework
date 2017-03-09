package com.example.inventory.domain.repository;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryEntryCount;
import com.example.inventory.domain.model.PlantInventoryItem;

import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
public interface CustomInventoryRepository {
    /**
     * Find PlantInventoryItems that are available (not rented)
     * after certain date in the past
     * @param period business period of interest
     * @return list of PlantInventoryItem
     */
    List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(BusinessPeriod period);
    List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(String entryId, BusinessPeriod period);
    List<PlantInventoryItem> findPlantItemsNotHiredInLastSixMonths();
    List<PlantInventoryEntryCount> findAndCountAvailablePlants(String name, BusinessPeriod period);
    List<PlantInventoryEntry> findAvailablePlants(String name, BusinessPeriod period);
    boolean itemAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period);
    boolean itemAvailableRelaxed(PlantInventoryEntry entry, BusinessPeriod period);
}