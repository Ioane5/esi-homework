package com.example.inventory.domain.repository;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryEntryCount;
import com.example.inventory.domain.model.PlantInventoryItem;
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

    @Deprecated
    //todo refactor dates issue
    public List<PlantInventoryEntryCount> findAndCountAvailablePlants(String name, BusinessPeriod period) {
        return em.createQuery("select new com.example.inventory.domain.model.PlantInventoryEntryCount(pe, count(p)) " +
                "from PlantInventoryItem p join p.plantInfo pe where p not in " +
                // where plant is not in (busy plant items).
                "(select pr.plant from PlantReservation pr " +
                "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant) " +
                "and p.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE " +
                "and LOWER(pe.name) like lower(?3) " +
                "group by pe", PlantInventoryEntryCount.class)
                .setParameter(1, period.getStartDate())
                .setParameter(2, period.getEndDate())
                .setParameter(3, "%" + name + "%")
                .getResultList();
    }

    @Override
    @Deprecated
    //todo refactor dates issue
    public boolean itemAvailableStrict(PlantInventoryEntry entry, BusinessPeriod period) {
        return em.createQuery("select count(p)" +
                "from PlantInventoryItem p where p not in " +
                // where plant is not in (busy plant items).
                "(select pr.plant from PlantReservation pr " +
                "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant) " +
                "and p.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE " +
                "and p.plantInfo = ?3", Long.class).setParameter(1, period.getStartDate())
                .setParameter(2, period.getEndDate())
                .setParameter(3, entry)
                .getSingleResult() > 0;
    }

    @Override
    @Deprecated
    public boolean itemAvailableRelaxed(PlantInventoryEntry entry, BusinessPeriod period) {
        return this.itemAvailableStrict(entry, period) ||
                (period.getStartDate().isAfter(LocalDate.now().plusWeeks(3)) && this.itemCheckRelaxed(entry, period));
    }

    private boolean itemCheckRelaxed(PlantInventoryEntry entry, BusinessPeriod period) {
        return em.createQuery("select count(pr.plant) from " +
                "PlantReservation pr " +
                "WHERE pr.maintenancePlan is not null " +
                "AND pr.plant.plantInfo = ?1 " +
                "AND pr.schedule.startDate > current_date " +
                "AND pr.schedule.endDate < ?2", Long.class)
                .setParameter(1, entry)
                .setParameter(2, period.getEndDate().minusWeeks(1))
                .getSingleResult() > 0;
    }

    private List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(BusinessPeriod period) {
        //noinspection unchecked
        return em.createQuery(
                "select p from PlantInventoryItem p where " +
                        "p not in " +
                        "(select r.plant from PlantReservation r where ?1 < r.schedule.endDate and ?2 > r.schedule.startDate)")
                .setParameter(1, period.getStartDate())
                .setParameter(2, period.getEndDate())
                .getResultList();

    }

    @Override
    public List<PlantInventoryItem> findAvailablePlantItemsInBusinessPeriod(String entryId, BusinessPeriod period) {
        //noinspection unchecked
        return em.createQuery(
                "select p from PlantInventoryItem p where p.plantInfo.id = ?1 and " +
                        "p.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and p not in " +
                        "(select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)")
                .setParameter(1, entryId)
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getResultList();
    }

    @Override
    public List<PlantInventoryItem> findPlantItemsNotHiredInLastSixMonths() {
        BusinessPeriod period = BusinessPeriod.of(LocalDate.now().minusMonths(6), LocalDate.now());
        return findAvailablePlantItemsInBusinessPeriod(period);
    }

    @Override
    public List<PlantInventoryEntry> findAvailablePlants(String name, BusinessPeriod period) {
        return em.createQuery(
                "select i.plantInfo from PlantInventoryItem i where lower(i.plantInfo.name) like ?1 and " +
                        "i.equipmentCondition = com.example.inventory.domain.model.EquipmentCondition.SERVICEABLE and " +
                        "i not in (select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)"
                , PlantInventoryEntry.class)
                .setParameter(1, "%" + name.toLowerCase() + "%")
                .setParameter(2, period.getStartDate())
                .setParameter(3, period.getEndDate())
                .getResultList();
    }
}