package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<PlantInventoryEntry, String>, CustomInventoryRepository {
}