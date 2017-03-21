package com.example.sales.web.controllers;

import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.services.PurchaseOrderAssembler;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.web.dto.CatalogQueryDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

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

        PurchaseOrderDTO po = new PurchaseOrderDTO();
        po.setRentalPeriod(query.getRentalPeriod());
        model.addAttribute("po", po);

        return "dashboard/catalog/query-result";
    }

    @PostMapping("sales/orders")
    public String createOrder(PurchaseOrderDTO orderDTO, Model model) {
        try {
            PurchaseOrder po = null;
            try {
                po = salesService.createPO(
                        plantInventoryEntryAssembler.fromResource(orderDTO.getPlant()),
                        businessPeriodAssembler.fromResource(orderDTO.getRentalPeriod())
                );
            } catch (PlantNotFoundException e) {
            }
            model.addAttribute("order", purchaseOrderAssembler.toResource(po));
        } catch (POValidationException e) {
            // TODO: handle invalid po data
            // return "dashboard/orders" ?!
        }
        return "dashboard/sales/purchase-order-result";
    }

}


