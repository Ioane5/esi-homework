package com.example.models;

import com.example.MainApplication;
import com.example.repositories.InventoryRepository;
import com.example.repositories.PlantInventoryItemRepository;
import com.example.repositories.PlantReservationRepository;
import com.example.repositories.PurchaseOrderRepository;
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
    private InventoryRepository inventoryRepo;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepo;
    @Autowired
    private PlantReservationRepository plantReservationRepo;
    @Autowired
    private PlantInventoryItemRepository plantInventoryItemRepo;

    @Test
    public void checkTests() {
        assertThat(true).isTrue();
    }

    @Test
    public void findAvailableTest() {
        assertThat(inventoryRepo.findAvailablePlants("Mini excavator", LocalDate.MIN, LocalDate.MAX)).hasSize(2);
    }

    @Test
    public void findPlantsNotHiredInLastSixMonthsWithReservationinPastTest() {
        int expectedSize = plantInventoryItemRepo.findAll().size() - 1;
        setUpPurchase(LocalDate.now().minusMonths(3), LocalDate.now().minusMonths(2));
        assertThat(inventoryRepo.findPlantsNotHiredInLastSixMonths())
                .hasSize(expectedSize);
    }

    @Test
    public void findPlantsNotHiredInLastSixMonthsWithReservationInFutureTest() {
        int expectedSize = plantInventoryItemRepo.findAll().size();
        setUpPurchase(LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(2));
        assertThat(inventoryRepo.findPlantsNotHiredInLastSixMonths())
                .hasSize(expectedSize);
    }

    private void setUpPurchase(LocalDate startDate, LocalDate endDate) {
        PurchaseOrder po = new PurchaseOrder();
        purchaseOrderRepo.save(po);
        PlantReservation r = new PlantReservation();
        r.setRental(po);
        r.setSchedule(BusinessPeriod.of(startDate, endDate));
        r.setPlant(plantInventoryItemRepo.findOne(1L));
        plantReservationRepo.save(r);
    }
}

