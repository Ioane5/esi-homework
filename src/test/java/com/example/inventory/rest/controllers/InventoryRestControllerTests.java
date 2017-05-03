package com.example.inventory.rest.controllers;

import com.example.MainApplication;
import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.maintenance.domain.repository.MaintenancePlanRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
@WebAppConfiguration
@DirtiesContext
public class InventoryRestControllerTests {
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;
    @Autowired
    PlantReservationRepository plantReservationRepository;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql("plants-dataset.sql")
    public void testGetAllPlants() throws Exception {
        setupReservation(LocalDate.now(), LocalDate.now().plusDays(3), "1");

        MvcResult result = mockMvc.perform(get("/api/inventory/plants?name=Exc&startDate=" + LocalDate.now().toString() + "&endDate=" + LocalDate.now().plusDays(7)))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", isEmptyOrNullString()))
                .andReturn();

        List<PlantInventoryEntryDTO> plants = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PlantInventoryEntryDTO>>() {
        });

        assertThat(plants.size()).isEqualTo(1);

    }

    private void setupReservation(LocalDate startDate, LocalDate endDate, String plantId) {
        PlantReservation r = PlantReservation.of(IdentifierFactory.nextId(), BusinessPeriod.of(startDate, endDate), plantInventoryItemRepository.findOne(plantId));
        plantReservationRepository.save(r);
    }
}
