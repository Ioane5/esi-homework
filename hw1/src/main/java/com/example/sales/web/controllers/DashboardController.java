package com.example.sales.web.controllers;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.InventoryRepository;
import com.example.inventory.web.dto.PlantInventoryEntryDTO;
import com.example.sales.application.services.SalesService;
import com.example.sales.web.dto.CatalogQueryDTO;
import com.example.sales.web.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    SalesService salesService;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    BusinessPeriodAssembler businessPeriodAssembler;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @GetMapping("/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }

    @PostMapping("catalog/query")
    public String executeQuery(CatalogQueryDTO query, Model model) {
        
        List<PlantInventoryEntry> availablePlants = inventoryService.findAvailablePlants(
                query.getName(),
                businessPeriodAssembler.fromResource(query.getRentalPeriod()));

        model.addAttribute("plants", plantInventoryEntryAssembler.toResources(availablePlants));
        model.addAttribute("po", new PurchaseOrderDTO());
        return "dashboard/catalog/query-result";
    }

    @PostMapping("sales/orders")
    public String createOrder(Model model) {
        PlantInventoryEntryDTO plant = new PlantInventoryEntryDTO();
        plant.setId("2");
        plant.setName("Ex2");
        plant.setDescription("Very cool ex2");
        plant.setPrice(BigDecimal.valueOf(700));

        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setPlant(plant);
        po.setStatus("OPEN");
        po.setTotal(BigDecimal.valueOf(600));
        po.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.of(2017, 4, 23)));

        model.addAttribute("order", po);
        return "dashboard/sales/purchase-order-result";
    }

}


