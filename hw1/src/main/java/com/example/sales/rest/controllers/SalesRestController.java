package com.example.sales.rest.controllers;


import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    InventoryService inventoryService;
    @Autowired
    SalesService salesService;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name") String plantName,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<PlantInventoryEntry> plants = inventoryService.findAvailablePlants(plantName, BusinessPeriod.of(startDate, endDate));
        return plantInventoryEntryAssembler.toResources(plants);
    }

//    @GetMapping("/orders/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) {
//        // TODO: Complete this part
//    }

//    @PostMapping("/orders")
//    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) {
//        PurchaseOrderDTO newlyCreatePODTO = ...
//        // TODO: Complete this part
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(new URI(newlyCreatePODTO.getId().getHref()));
//        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport
//
//        return new ResponseEntity<PurchaseOrderDTO>(newlyCreatePODTO, headers, HttpStatus.CREATED);
//    }
}
