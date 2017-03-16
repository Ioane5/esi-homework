package com.example.maintenance.domain.repository;

import com.example.MainApplication;
import com.example.maintenance.domain.model.CorrectiveRepairCostYearlyRecord;
import com.example.maintenance.domain.model.CorrectiveRepairCountYearlyRecord;
import com.example.maintenance.domain.repository.MaintenanceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by vkop on 22-Feb-17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
@Sql(scripts = "maintenance-dataset.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MaintenanceRepositoryTests {

    @Autowired
    MaintenanceRepository maintenanceRepo;

    @Test
    public void shouldCountCorrectiveRepairs() {
        assertThat(maintenanceRepo.findNumberOfCorrectiveRepairsByYear()).containsOnlyOnce(new CorrectiveRepairCountYearlyRecord(2017, 2));
    }

    @Test
    public void shouldCountCorrectiveRepairsPerYearFor5Years() {
        assertThat(maintenanceRepo.findNumberOfCorrectiveRepairsByYear()).hasSize(5);
    }

    @Test
    public void shouldNotCountCorrectiveRepairsLaterThan5Years() {
        assertThat(maintenanceRepo.findNumberOfCorrectiveRepairsByYear()).doesNotContain(new CorrectiveRepairCountYearlyRecord(2012, 1));
    }


    @Test
    public void shouldCalculateCorrectiveRepairsCost() {
        assertThat(maintenanceRepo.findCostOfCorrectiveRepairsByYear()).first()
                .isEqualTo(new CorrectiveRepairCostYearlyRecord(2017, new BigDecimal("200.00")));

    }

    @Test
    public void shouldCalculateCorrectiveRepairsCostPerYearFor5Years() {
        assertThat(maintenanceRepo.findCostOfCorrectiveRepairsByYear()).hasSize(5);
    }

    @Test
    public void shouldNotSumCorrectiveRepairsCostLaterThan5Years() {
        assertThat(maintenanceRepo.findCostOfCorrectiveRepairsByYear()).doesNotContain(new CorrectiveRepairCostYearlyRecord(2012, BigDecimal.valueOf(300.00)));
    }
}
