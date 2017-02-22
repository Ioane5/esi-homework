package com.example.models;

import com.example.MainApplication;
import com.example.repositories.MaintenanceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    public void shouldCountCorrectiveRepairsPerYear() {
        assertThat(maintenanceRepo.findNumberOfCorrectiveRepairsByYear()).hasSize(3);
    }

    @Test
    public void shouldCountCorrectiveRepairsCostPerYear() {
        assertThat(maintenanceRepo.findCostOfCorrectiveRepairsByYear()).hasSize(3);
    }
}
