package com.example.repositories;

import com.example.models.PlantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ioane5 on 2/20/17.
 */
@Repository
public interface PlantReservationRepository extends JpaRepository<PlantReservation, Long> {
}