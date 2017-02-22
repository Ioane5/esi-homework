package com.example.repositories;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
public interface CustomInventoryRepository {
    /**
     * @return Object[0] = PlantInventoryEntry, Object[1] = count of inventory items
     */
    List<Object[]> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);
}