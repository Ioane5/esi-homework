package com.example.maintenance.domain.repository;

import com.example.maintenance.domain.model.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MaintenanceRepository extends JpaRepository<MaintenancePlan, String>, CustomMaintenanceRepository {
}
