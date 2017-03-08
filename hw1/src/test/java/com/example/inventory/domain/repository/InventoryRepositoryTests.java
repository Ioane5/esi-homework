package com.example.inventory.domain.repository;

import com.example.MainApplication;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.domain.model.PurchaseOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
@Sql(scripts = "plants-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InventoryRepositoryTests {
    @Autowired
    private InventoryRepository inventoryRepo;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepo;
    @Autowired
    private PlantReservationRepository plantReservationRepo;
    @Autowired
    private PlantInventoryItemRepository plantInventoryItemRepo;


    @Test
    public void findAvailableTest_SelectExcavators() {
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 1, 1), LocalDate.of(2018, 1, 1));
        assertThat(inventoryRepo.findAndCountAvailablePlants("excavator", period))
                .hasSize(3);
    }

    @Test
    public void findAvailableTest_SelectEveryWorkingItem() {
        BusinessPeriod period = BusinessPeriod.of(LocalDate.MAX, LocalDate.MAX);
        assertThat(inventoryRepo.findAndCountAvailablePlants("", period))
                .hasSize(5);
    }

    @Test
    public void findAvailableTest_SelectEveryWorkingAndUnusedEntry() {
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(1980, 1, 1), LocalDate.of(2480, 1, 1));
        assertThat(inventoryRepo.findAndCountAvailablePlants("", period))
                .hasSize(3);
    }

    @Test
    public void findAvailableTest_SelectAllWorkingDumpers() {
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 6, 1), LocalDate.of(2017, 9, 1));
        assertThat(inventoryRepo.findAndCountAvailablePlants("dumper", period))
                .hasSize(2);
    }

    @Test
    public void checkItemAvailabilityStrictWhenNotAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne("3");
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2009, 3, 22), LocalDate.of(2009, 3, 23));
        boolean actual = inventoryRepo.itemAvailableStrict(pe, period);
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void checkItemAvailabilityStrictWhenAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne("3");
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2008, 3, 22), LocalDate.of(2008, 3, 23));
        boolean actual = inventoryRepo.itemAvailableStrict(pe, period);
        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void checkItemAvailabilityRelaxedWhenNearFuture() {
        PlantInventoryEntry pe = inventoryRepo.findOne("5");
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 3, 3), LocalDate.of(2017, 3, 5));
        boolean actual = inventoryRepo.itemAvailableRelaxed(pe, period);
        assertThat(actual).isEqualTo(false);
    }


    @Test
    public void checkItemAvailabilityRelaxedWhenNotAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne("6");
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 4, 3), LocalDate.of(2017, 4, 5));
        boolean actual = inventoryRepo.itemAvailableRelaxed(pe, period);
        assertThat(actual).isEqualTo(false);
    }

    @Test
    public void checkItemAvailabilityRelaxedWhenAvailableTest() {
        PlantInventoryEntry pe = inventoryRepo.findOne("6");
        BusinessPeriod period = BusinessPeriod.of(LocalDate.of(2017, 4, 15), LocalDate.of(2017, 4, 20));
        boolean actual = inventoryRepo.itemAvailableRelaxed(pe, period);
        assertThat(actual).isEqualTo(true);
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
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextId(),null,null,null);
        purchaseOrderRepo.save(po);
        PlantReservation r = PlantReservation.of(IdentifierFactory.nextId(), BusinessPeriod.of(startDate, endDate), plantInventoryItemRepo.findOne("1"))
                .withPurchaseOrder(po);
        plantReservationRepo.save(r);
    }
}

