package com.example.maintenance.domain.repository;

import com.example.maintenance.domain.model.CorrectiveRepairCostYearlyRecord;
import com.example.maintenance.domain.model.CorrectiveRepairCountYearlyRecord;
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
        return em.createQuery("select new com.example.maintenance.domain.model.CorrectiveRepairCountYearlyRecord(mp.yearOfAction, count(t)) " +
                "from MaintenancePlan mp LEFT JOIN mp.tasks t where t.typeOfWork = com.example.maintenance.domain.model.TypeOfWork.CORRECTIVE " +
                "GROUP BY mp.yearOfAction " +
                "ORDER BY mp.yearOfAction DESC ", CorrectiveRepairCountYearlyRecord.class)
                .setMaxResults(5)
                .getResultList();
    }

    @Override
    public List<CorrectiveRepairCostYearlyRecord> findCostOfCorrectiveRepairsByYear() {
        return em.createQuery("select new com.example.maintenance.domain.model.CorrectiveRepairCostYearlyRecord(mp.yearOfAction, SUM(t.price)) " +
                "from MaintenancePlan mp LEFT JOIN mp.tasks t where t.typeOfWork = com.example.maintenance.domain.model.TypeOfWork.CORRECTIVE " +
                "GROUP BY mp.yearOfAction " +
                "ORDER BY mp.yearOfAction DESC ", CorrectiveRepairCostYearlyRecord.class)
                .setMaxResults(5)
                .getResultList();
    }
}
