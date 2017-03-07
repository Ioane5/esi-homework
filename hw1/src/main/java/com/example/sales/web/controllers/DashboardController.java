package com.example.sales.web.controllers;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.sales.web.dto.CatalogQueryDTO;
import com.example.sales.web.dto.PurchaseOrderDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by vkop on 03-Mar-17.
 */


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

        return "dashboard/catalog/query-result";
    }

    @PostMapping("sales/orders")
    public String createOrder(Model model) {
        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setPlantDescription("Description");
        po.setPlantName("Excavator");
        po.setStatus("OPEN");
        po.setTotal(BigDecimal.valueOf(600));
        po.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.of(2017, 4, 23)));

        model.addAttribute("order", po);
        return "dashboard/sales/purchase-order-result";
    }

}


