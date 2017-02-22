package com.example.repositories;

import com.example.models.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vkop on 22-Feb-17.
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenancePlan, Long>, CustomMaintenanceRepository {
}
