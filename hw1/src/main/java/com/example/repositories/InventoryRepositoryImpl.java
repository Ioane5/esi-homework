package com.example.repositories;

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

    public List findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        System.out.println("findAvailablePlants() called with: name = [" + name + "], startDate = [" + startDate + "], endDate = [" + endDate + "]");
        List<PlantInventoryItem> minus =
                em.createQuery("select pi from PlantInventoryItem pi  where pi not in" +
                                "(select pr.plant from PlantReservation pr " +
                                "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant)"
                        , PlantInventoryItem.class)
                        .setParameter(1, startDate)
                        .setParameter(2, endDate)
                        .getResultList();
        System.out.println("Minus selected ");
        minus.forEach((i) -> {
            System.out.println(i);
        });
        System.out.println("Selected ");

        List<Object[]> selected =
                em.createQuery("select pe, count(p) " +
                        "from PlantInventoryItem p join p.plantInfo pe where p not in " +
                        "(select pr.plant from PlantReservation pr " +
                        "where not(pr.schedule.startDate > ?2 or pr.schedule.endDate < ?1) group by pr.plant) " +
                        "and p.equipmentCondition = 'SERVICEABLE' " +
                        "and LOWER(pe.name) like lower(?3) " +
                        "group by pe", Object[].class)
                        .setParameter(1, startDate)
                        .setParameter(2, endDate)
                        .setParameter(3, "%" + name + "%")
                        .getResultList();
        selected.forEach((i) -> {
            System.out.println(i[0]);
            System.out.println(i[1]);
        });
        return selected;
    }
}