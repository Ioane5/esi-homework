package com.example.sales.rest.controllers;

import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.PurchaseOrderAssembler;
import com.example.sales.application.services.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    SalesService salesService;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

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
}
