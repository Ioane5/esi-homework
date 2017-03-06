package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantInventoryEntryCount;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;

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
    List<PlantInventoryItem> findAvailablePlantsInBusinessPeriod(BusinessPeriod period);
    List<PlantInventoryItem> findPlantsNotHiredInLastSixMonths();
    List<PlantInventoryEntryCount> findAvailablePlants(String name, BusinessPeriod period);
    boolean itemAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period);
    boolean itemAvailableRelaxed(PlantInventoryEntry entry, BusinessPeriod period);
}