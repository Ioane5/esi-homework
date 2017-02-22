package com.example.repositories;

import com.example.models.BusinessPeriod;
import com.example.models.PlantInventoryEntry;
import com.example.models.PlantInventoryItem;
import org.apache.tomcat.jni.Local;
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

    public List findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        List selected =
                em.createQuery("select p from PlantInventoryItem p " +
                        "where p.equipmentCondition = 'SERVICEABLE' " +
                        "and LOWER(p.plantInfo.name) like lower(?1) " +
                        "group by p.plantInfo")
                        .setParameter(1, name)
                        .getResultList();
        selected.forEach((i) -> System.out.println(">> " + i));
        return selected;
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