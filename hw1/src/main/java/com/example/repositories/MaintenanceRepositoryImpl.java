package com.example.repositories;

import com.example.dto.CorrectiveRepairCostYearlyRecord;
import com.example.dto.CorrectiveRepairCountYearlyRecord;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by vkop on 22-Feb-17.
 */
@SuppressWarnings("JpaQlInspection")
public class MaintenanceRepositoryImpl implements CustomMaintenanceRepository {
    @Autowired
    EntityManager em;

    @Override
    public List<CorrectiveRepairCountYearlyRecord> findNumberOfCorrectiveRepairsByYear() {
        List<CorrectiveRepairCountYearlyRecord> list =
                em.createQuery("select new com.example.dto.CorrectiveRepairCountYearlyRecord(mp.yearOfAction, count(t)) " +
                        "from MaintenancePlan mp LEFT JOIN mp.tasks t where t.typeOfWork = 'CORRECTIVE' " +
                        "GROUP BY mp.yearOfAction " +
                        "ORDER BY mp.yearOfAction DESC ", CorrectiveRepairCountYearlyRecord.class)
                        .setMaxResults(5)
                        .getResultList();
        return list;
    }

    @Override
    public List<CorrectiveRepairCostYearlyRecord> findCostOfCorrectiveRepairsByYear() {
        List<CorrectiveRepairCostYearlyRecord> list =
                em.createQuery("select new com.example.dto.CorrectiveRepairCostYearlyRecord(mp.yearOfAction, SUM(t.price)) " +
                        "from MaintenancePlan mp LEFT JOIN mp.tasks t where t.typeOfWork = 'CORRECTIVE' " +
                        "GROUP BY mp.yearOfAction " +
                        "ORDER BY mp.yearOfAction DESC ", CorrectiveRepairCostYearlyRecord.class)
                        .setMaxResults(5)
                        .getResultList();
        return list;
    }
}
