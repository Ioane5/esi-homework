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
    public void checkTests() {
        assertThat(true).isTrue();
    }

    @Test
    public void findAvailableTest() {
        assertThat(inventoryRepo.findAvailablePlants("Mini excavator", LocalDate.MIN, LocalDate.MAX)).hasSize(2);
    }
}

