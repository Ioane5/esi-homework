package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ioane5 on 2/20/17.
 */
@Repository
public interface InventoryRepository extends JpaRepository<PlantInventoryEntry, Long>, CustomInventoryRepository {
}