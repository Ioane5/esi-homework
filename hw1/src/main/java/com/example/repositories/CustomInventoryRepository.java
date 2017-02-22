package com.example.repositories;

import com.example.models.BusinessPeriod;
import com.example.models.PlantInventoryEntry;
import com.example.models.PlantInventoryItem;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
public interface CustomInventoryRepository {
    List<PlantInventoryEntry> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);

    /**
     * Find PlantInventoryItems that are available (not rented)
     * after certain date in the past
     * @param period business period of interest
     * @return list of PlantInventoryItem
     */
    List<PlantInventoryItem> findAvailablePlantsInBusinessPeriod(BusinessPeriod period);
    List<PlantInventoryItem> findPlantsNotHiredInLastSixMonths();
}