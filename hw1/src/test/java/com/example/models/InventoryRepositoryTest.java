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
@Sql(scripts = "/plants-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTest {

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
    public void findAvailableTest_SelectAllWorkingdDumpers() {
        assertThat(inventoryRepo.findAvailablePlants("dumper", LocalDate.of(2017, 6, 1), LocalDate.of(2017, 9, 1)))
                .hasSize(2);
    }

    @Test
    public void checkItemAvailabilityStrictWhenNotAvailableTest(){
        PlantInventoryEntry pe = new PlantInventoryEntry();
        pe.setId(3L);

        boolean actual = inventoryRepo.itemAvailableStrict(pe, LocalDate.of(2009, 3, 22), LocalDate.of(2009, 3, 23));
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void checkItemAvailabilityStrictWhenAvailableTest(){
        PlantInventoryEntry pe = new PlantInventoryEntry();
        pe.setId(3L);

        boolean actual = inventoryRepo.itemAvailableStrict(pe, LocalDate.of(2008, 3, 22), LocalDate.of(2008, 3, 23));
        assertThat(actual).isEqualTo(true);
    }
}

