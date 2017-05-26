package com.example.inventory.domain.repository;

import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantReservationRepository extends JpaRepository<PlantReservation, String> {

    List<PlantReservation> findAllByPlant(PlantInventoryItem item);

}