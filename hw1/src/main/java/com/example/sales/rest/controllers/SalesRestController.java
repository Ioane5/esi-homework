package com.example.sales.rest.controllers;


import com.example.common.application.exceptions.PlantNotAvailableException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.PurchaseOrderAssembler;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    SalesService salesService;
    @Autowired
    PurchaseOrderAssembler poAssembler;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    BusinessPeriodAssembler businessPeriodAssembler;

//    @GetMapping("/orders/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) {
//        // TODO: Complete this part
//    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws PlantNotAvailableException {
        PlantInventoryEntry plant = plantInventoryEntryAssembler.fromResource(partialPODTO.getPlant());
        BusinessPeriod period = businessPeriodAssembler.fromResource(partialPODTO.getRentalPeriod());

        PurchaseOrder purchaseOrder = salesService.createPO(plant, period);
        if (purchaseOrder.getStatus() == POStatus.REJECTED || TextUtils.isEmpty(purchaseOrder.getId())) {
            throw new PlantNotAvailableException("The Plant is not available");
        }
        PurchaseOrderDTO newlyCreatePODTO = poAssembler.toResource(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        newlyCreatePODTO.getId().getHref();
        try {
            headers.setLocation(new URI(newlyCreatePODTO.getId().getHref()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(newlyCreatePODTO, headers, HttpStatus.CREATED);
    }

    @ExceptionHandler(PlantNotAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotAvailableException(PlantNotAvailableException ex) {
    }
}
