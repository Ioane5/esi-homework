package com.example.sales.web.controllers;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.web.dto.PlantInventoryEntryDTO;
import com.example.sales.web.dto.CatalogQueryDTO;
import com.example.sales.web.dto.PurchaseOrderDTO;
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
    @GetMapping("/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }

    @PostMapping("catalog/query")
    public String executeQuery(CatalogQueryDTO query, Model model) {
        PlantInventoryEntryDTO p1 = new PlantInventoryEntryDTO();
        p1.setId("1");
        p1.setName("Ex1");
        p1.setDescription("Very cool ex1");
        p1.setPrice(BigDecimal.valueOf(400));

        PlantInventoryEntryDTO p2 = new PlantInventoryEntryDTO();
        p2.setId("2");
        p2.setName("Ex2");
        p2.setDescription("Very cool ex2");
        p2.setPrice(BigDecimal.valueOf(700));

        List<PlantInventoryEntryDTO> plants = new ArrayList<>();
        plants.add(p1);
        plants.add(p2);

        model.addAttribute("plants", plants);
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


