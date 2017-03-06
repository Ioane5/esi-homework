package com.example.maintenance.domain.repository;

import com.example.maintenance.domain.model.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by vkop on 22-Feb-17.
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<MaintenancePlan, Long>, CustomMaintenanceRepository {
}
