package com.example.inventory.domain.repository;

import com.example.MainApplication;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import com.example.maintenance.domain.repository.MaintenancePlanRepository;
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
    @Autowired
    private MaintenancePlanRepository maintenancePlanRepository;

    @Test
    public void findPlantsNotHiredInLastSixMonthsWithReservationInPastTest() {
        int expectedSize = plantInventoryItemRepo.findAll().size() - 1;
        setUpPurchase(LocalDate.now().minusMonths(3), LocalDate.now().minusMonths(2));
        assertThat(inventoryRepo.findPlantItemsNotHiredInLastSixMonths())
                .hasSize(expectedSize);
    }

    @Test
    public void findPlantsNotHiredInLastSixMonthsWithReservationInFutureTest() {
        int expectedSize = plantInventoryItemRepo.findAll().size();
        setUpPurchase(LocalDate.now().plusMonths(3), LocalDate.now().plusMonths(2));
        assertThat(inventoryRepo.findPlantItemsNotHiredInLastSixMonths())
                .hasSize(expectedSize);
    }

    private void setUpPurchase(LocalDate startDate, LocalDate endDate) {
        PurchaseOrder po = PurchaseOrder.of(IdentifierFactory.nextId(),null,null,null);
        purchaseOrderRepo.save(po);
        PlantReservation r = PlantReservation.of(IdentifierFactory.nextId(), BusinessPeriod.of(startDate, endDate), plantInventoryItemRepo.findOne("1"))
                .withPurchaseOrder(po);
        plantReservationRepo.save(r);
    }

    private void setUpReservation(LocalDate startDate, LocalDate endDate, String plant_id, String maintenance_id) {
        PlantReservation r = PlantReservation.of(IdentifierFactory.nextId(), BusinessPeriod.of(startDate, endDate), plantInventoryItemRepo.findOne(plant_id));

        if(maintenance_id != null) {
            r.withMaintenancePlan(maintenancePlanRepository.findOne(maintenance_id));
        }

        plantReservationRepo.save(r);
    }

    private void setUpReservations() {
        setUpReservation(LocalDate.now().plusDays(13), LocalDate.now().plusDays(15), "1", null);

        setUpReservation(LocalDate.now().minusDays(8), LocalDate.now().plusDays(16), "12", "1");
        setUpReservation(LocalDate.now().plusMonths(1), LocalDate.now().plusMonths(1), "13", "1");
    }
}

