package com.example.sales.rest.controllers;

import com.example.MainApplication;
import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MainApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SalesRestControllerTests {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    @Test
    @Sql("plants-dataset.sql")
    public void testCheckValidPurchaseOrder() throws Exception {
        PlantInventoryEntry pe = plantInventoryEntryRepository.findOne("1");

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plantInventoryEntryAssembler.toResource(pe));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 4, 1)));

        mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Sql("plants-dataset.sql")
    public void testCheckRentedPurchaseOrderCreation() throws Exception {
        PlantInventoryEntry pe = plantInventoryEntryRepository.findOne("2");

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plantInventoryEntryAssembler.toResource(pe));
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2017, 1, 1), LocalDate.of(2017, 4, 1)));

        mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}

