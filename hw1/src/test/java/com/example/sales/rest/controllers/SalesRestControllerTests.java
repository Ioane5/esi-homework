package com.example.sales.rest.controllers;

import com.example.MainApplication;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.cglib.core.Local;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "plants-dataset.sql")
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

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

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
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusDays(2)));

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
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusDays(2)));

        mockMvc.perform(post("/api/sales/orders")
                .content(mapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllPurchaseOrders() throws Exception {
        setUpOrders();
        MvcResult result = mockMvc.perform(get("/api/sales/orders"))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", isEmptyOrNullString()))
                .andReturn();

        List<PurchaseOrderDTO> orders = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<PurchaseOrderDTO>>() {});

        assertThat(orders.size()).isEqualTo(1);
        assertThat(orders.get(0).get_id().equals("1"));
    }

    @Test
    public void testGetPurchaseOrder() throws Exception {
        setUpOrders();
        MvcResult result = mockMvc.perform(get("/api/sales/orders/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", isEmptyOrNullString()))
                .andReturn();

        PurchaseOrderDTO orders = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<PurchaseOrderDTO>() {});

        assertThat(orders.get_id().equals("1"));
    }

    @Test
    public void testGetNonExistentPurchaseOrder() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/sales/orders/2"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private void setUpOrders(){
        PurchaseOrder po = PurchaseOrder.of("1", plantInventoryEntryRepository.findOne("1") , LocalDate.now(), BusinessPeriod.of(LocalDate.now(), LocalDate.now()));
        purchaseOrderRepository.save(po);
    }
}

