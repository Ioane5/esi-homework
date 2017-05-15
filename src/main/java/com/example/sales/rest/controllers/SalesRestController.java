package com.example.sales.rest.controllers;

import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.dto.CustomerDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.CustomerService;
import com.example.sales.application.services.PurchaseOrderAssembler;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.model.Customer;
import com.example.sales.domain.model.POStatus;
import com.example.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    private SalesService salesService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PurchaseOrderAssembler poAssembler;
    @Autowired
    private PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    private BusinessPeriodAssembler businessPeriodAssembler;


    @GetMapping("/orders/{id}")

    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) throws PurchaseOrderNotFoundException {
        return poAssembler.toResource(salesService.findPO(id));
    }

    @GetMapping("/orders")
    public List<PurchaseOrderDTO> fetchPurchaseOrders() {
        return poAssembler.toResources(salesService.findAllPOs());
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody PurchaseOrderDTO partialPODTO)
            throws POValidationException {
        PlantInventoryEntry plant = plantInventoryEntryAssembler.fromResource(partialPODTO.getPlant());
        BusinessPeriod period = businessPeriodAssembler.fromResource(partialPODTO.getRentalPeriod());
        Customer customer = customerService.retrieveCustomer(token);

        PurchaseOrder purchaseOrder = salesService.createPO(customer, plant, period);
        PurchaseOrderDTO newlyCreatePODTO = poAssembler.toResource(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.setLocation(new URI(newlyCreatePODTO.getId().getHref()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpStatus status = newlyCreatePODTO.getStatus() == POStatus.REJECTED ? HttpStatus.NOT_FOUND : HttpStatus.CREATED;
        return new ResponseEntity<>(newlyCreatePODTO, headers, status);
    }

    @PostMapping("/orders/{id}/accept")
    public PurchaseOrderDTO acceptPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.acceptPurchaseOrder(id));
    }

    @DeleteMapping("/orders/{id}/accept")
    public PurchaseOrderDTO rejectPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.rejectPurchaseOrder(id));
    }

    @DeleteMapping("/orders/{id}")
    public PurchaseOrderDTO closePurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.closePurchaseOrder(id));
    }

    @PostMapping("/customers")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO partialCustomerDto) throws Exception {
        Customer customer = customerService.createCustomer(partialCustomerDto.getEmail());
        return CustomerDTO.of(customer.getId(), customer.getToken(), customer.getEmail());
    }

//    @ExceptionHandler(PlantNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public void handlePlantNotFoundException(PlantNotFoundException ex) {
//    }

    @ExceptionHandler(POValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlePOValidationException(POValidationException ex) {
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
    }
}
