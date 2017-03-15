package com.example.sales.rest.controllers;

import com.example.common.application.exceptions.PurchaseOrderNotFoundException;

import com.example.common.application.exceptions.POValidationException;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    SalesService salesService;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    PurchaseOrderAssembler poAssembler;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    BusinessPeriodAssembler businessPeriodAssembler;

    @GetMapping("/orders/{id}")
    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) throws PurchaseOrderNotFoundException {
        return purchaseOrderAssembler.toResource(salesService.findPO(id));
    }

    @GetMapping("/orders")
    public List<PurchaseOrderDTO> fetchPurchaseOrders() {
        return purchaseOrderAssembler.toResources(salesService.findAllPOs());
    }

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

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePOValidationException(PurchaseOrderNotFoundException ex) {
    }
    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws PlantNotAvailableException, POValidationException {
        PlantInventoryEntry plant = plantInventoryEntryAssembler.fromResource(partialPODTO.getPlant());
        BusinessPeriod period = businessPeriodAssembler.fromResource(partialPODTO.getRentalPeriod());

        PurchaseOrder purchaseOrder = salesService.createPO(plant, period);
        if (purchaseOrder.getStatus() == POStatus.REJECTED || TextUtils.isEmpty(purchaseOrder.getId())) {
            throw new PlantNotAvailableException("The Plant is not available");
        }
        PurchaseOrderDTO newlyCreatePODTO = poAssembler.toResource(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(new URI(newlyCreatePODTO.getId().getHref()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(newlyCreatePODTO, headers, HttpStatus.CREATED);
    }

    @ExceptionHandler(PlantNotAvailableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePlantNotAvailableException(PlantNotAvailableException ex) {
    }

    @ExceptionHandler(POValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlePOValidationException(POValidationException ex) {
    }
}
