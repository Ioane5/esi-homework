package com.example.maintenance.domain.repository;

import com.example.maintenance.domain.model.CorrectiveRepairCostYearlyRecord;
import com.example.maintenance.domain.model.CorrectiveRepairCountYearlyRecord;

import java.util.List;

/**
 * Created by vkop on 22-Feb-17.
 */
public interface CustomMaintenanceRepository {
    List<CorrectiveRepairCountYearlyRecord> findNumberOfCorrectiveRepairsByYear();
    List<CorrectiveRepairCostYearlyRecord> findCostOfCorrectiveRepairsByYear();

}
