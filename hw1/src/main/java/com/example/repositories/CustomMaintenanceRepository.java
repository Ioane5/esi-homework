package com.example.repositories;

import com.example.dto.CorrectiveRepairCostYearlyRecord;
import com.example.dto.CorrectiveRepairCountYearlyRecord;

import java.util.List;

/**
 * Created by vkop on 22-Feb-17.
 */
public interface CustomMaintenanceRepository {
    List<CorrectiveRepairCountYearlyRecord> findNumberOfCorrectiveRepairsByYear();
    List<CorrectiveRepairCostYearlyRecord> findCostOfCorrectiveRepairsByYear();

}
