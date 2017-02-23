package com.example.repositories;

import com.example.dto.PlantInventoryEntryCount;
import com.example.models.PlantInventoryEntry;
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

    public List<PlantInventoryEntryCount> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select new com.example.dto.PlantInventoryEntryCount(pe, count(p)) " +
                "from PlantInventoryItem p join p.plantInfo pe where p not in " +
                // where plant is not in (busy plant items).
                "(select pr.plant from PlantReservation pr " +
                "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant) " +
                "and p.equipmentCondition = 'SERVICEABLE' " +
                "and LOWER(pe.name) like lower(?3) " +
                "group by pe", PlantInventoryEntryCount.class)
                .setParameter(1, startDate)
                .setParameter(2, endDate)
                .setParameter(3, "%" + name + "%")
                .getResultList();
    }

    @Override
    public boolean itemAvailableStrict(PlantInventoryEntry entry, LocalDate startDate, LocalDate endDate) {
        return em.createQuery("select count(p)" +
                "from PlantInventoryItem p where p not in " +
                // where plant is not in (busy plant items).
                "(select pr.plant from PlantReservation pr " +
                "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant) " +
                "and p.equipmentCondition = 'SERVICEABLE' " +
                "and p.plantInfo.id = ?3", Long.class).setParameter(1, startDate)
                .setParameter(2, endDate)
                .setParameter(3, entry.getId())
                .getSingleResult() > 0;
    }
}