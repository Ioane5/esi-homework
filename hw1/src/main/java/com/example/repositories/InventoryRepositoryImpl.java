package com.example.repositories;

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
}