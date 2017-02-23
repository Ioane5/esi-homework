package com.example.models;

import com.example.MainApplication;
import com.example.repositories.InventoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ioane5 on 2/20/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
@Sql(scripts = "plants-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTests {

    @Autowired
    InventoryRepository inventoryRepo;

    @Test
    public void findAvailableTest_SelectExcavators() {
        assertThat(inventoryRepo.findAvailablePlants("excavator", LocalDate.of(2017, 1, 1), LocalDate.of(2018, 1, 1)))
                .hasSize(3);
    }

    @Test
    public void findAvailableTest_SelectEveryWorkingItem() {
        assertThat(inventoryRepo.findAvailablePlants("", LocalDate.MAX, LocalDate.MAX))
                .hasSize(5);
    }

    @Test
    public void findAvailableTest_SelectEveryWorkingAndUnusedEntry() {
        assertThat(inventoryRepo.findAvailablePlants("", LocalDate.of(1980, 1, 1), LocalDate.of(2480, 1, 1)))
                .hasSize(3);
    }

    @Test
    public void findAvailableTest_SelectAllWorkingDumpers() {
        assertThat(inventoryRepo.findAvailablePlants("dumper", LocalDate.of(2017, 6, 1), LocalDate.of(2017, 9, 1)))
                .hasSize(2);
    }

    @Test
    public void checkItemAvailabilityStrictWhenNotAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne(3L);
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2009, 3, 22), LocalDate.of(2009, 3, 23));
        boolean actual = inventoryRepo.itemAvailableStrict(pe, period);
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void checkItemAvailabilityStrictWhenAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne(3L);
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2008, 3, 22), LocalDate.of(2008, 3, 23));
        boolean actual = inventoryRepo.itemAvailableStrict(pe, period);
        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void checkItemAvailabilityRelaxedWhenNearFuture() {
        PlantInventoryEntry pe = inventoryRepo.findOne(5L);
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 3, 3), LocalDate.of(2017, 3, 5));
        boolean actual = inventoryRepo.itemAvailableRelaxed(pe, period);
        assertThat(actual).isEqualTo(false);
    }


    @Test
    public void checkItemAvailabilityRelaxedWhenNotAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne(6L);
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 4, 3), LocalDate.of(2017, 4, 5));
        boolean actual = inventoryRepo.itemAvailableRelaxed(pe, period);
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void checkItemAvailabilityRelaxedWhenAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne(6L);
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 4, 15), LocalDate.of(2017, 4, 20));
        boolean actual = inventoryRepo.itemAvailableRelaxed(pe, period);
        assertThat(actual).isEqualTo(true);
    }
}

