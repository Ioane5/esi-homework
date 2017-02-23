package com.example.repositories;

import com.example.dto.PlantInventoryEntryCount;
import com.example.models.BusinessPeriod;
import com.example.models.PlantInventoryItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
@SuppressWarnings("JpaQlInspection")
public class InventoryRepositoryImpl implements CustomInventoryRepository {
    @Autowired
    EntityManager em;

    public List<PlantInventoryEntryCount> findAvailablePlants(String name, BusinessPeriod period) {
        return em.createQuery("select new com.example.dto.PlantInventoryEntryCount(pe, count(p)) " +
                "from PlantInventoryItem p join p.plantInfo pe where p not in " +
                // where plant is not in (busy plant items).
                "(select pr.plant from PlantReservation pr " +
                "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant) " +
                "and p.equipmentCondition = 'SERVICEABLE' " +
                "and LOWER(pe.name) like lower(?3) " +
                "group by pe", PlantInventoryEntryCount.class)
                .setParameter(1, period.getStartDate())
                .setParameter(2, period.getEndDate())
                .setParameter(3, "%" + name + "%")
                .getResultList();
    }

    @Override
    public List<PlantInventoryItem> findAvailablePlantsInBusinessPeriod(BusinessPeriod period) {
        //noinspection unchecked
        return em.createQuery(
                        "select p from PlantInventoryItem p where p not in (" +
                            "select r.plant from PlantReservation r " +
                                "where r.schedule.startDate > ?1 and r.schedule.startDate < ?2 and r.rental IS NOT NULL)")
                        .setParameter(1, period.getStartDate())
                        .setParameter(2, period.getEndDate())
                        .getResultList();

    }

    @Override
    public List<PlantInventoryItem> findPlantsNotHiredInLastSixMonths() {
        BusinessPeriod period = BusinessPeriod.of(LocalDate.now().minusMonths(6), LocalDate.now());
        return findAvailablePlantsInBusinessPeriod(period);
    }
}