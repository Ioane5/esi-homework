package com.example.repositories;

import com.example.models.PlantInventoryEntry;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
public interface CustomInventoryRepository {
    List<PlantInventoryEntry> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);
}