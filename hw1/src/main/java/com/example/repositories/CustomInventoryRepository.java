package com.example.repositories;

import com.example.dto.PlantInventoryEntryCount;
import com.example.models.BusinessPeriod;
import com.example.models.PlantInventoryEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
public interface CustomInventoryRepository {

    List<PlantInventoryEntryCount> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);
    boolean itemAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period);
    boolean itemAvailableRelaxed(PlantInventoryEntry entry, BusinessPeriod period);
}