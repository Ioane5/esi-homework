package com.example.sales.rest.controllers;

import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.dto.CustomerDTO;
import com.example.sales.application.dto.PORequestDTO;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.CustomerService;
import com.example.sales.application.services.PurchaseOrderAssembler;
import com.example.sales.application.services.SalesService;
import com.example.sales.domain.model.Customer;
import com.example.sales.domain.model.POStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
    public PurchaseOrderDTO fetchPurchaseOrder(@RequestHeader("Authorization") String token,
                                               @PathVariable("id") String id) throws PurchaseOrderNotFoundException {
        Customer customer = customerService.retrieveCustomer(token);
        return poAssembler.toResource(salesService.findPO(id, customer));
    }

    @GetMapping("/orders")
    public List<PurchaseOrderDTO> fetchPurchaseOrders(@RequestHeader("Authorization") String token) {
        Customer customer = customerService.retrieveCustomer(token);
        return poAssembler.toResources(salesService.findAllPOs(customer));
    }

    @PostMapping("/orders")
    public PurchaseOrderDTO createPurchaseOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody PORequestDTO poRequestDTO)
            throws POValidationException {

        Customer customer = customerService.retrieveCustomer(token);
        BusinessPeriod period = businessPeriodAssembler.fromResource(poRequestDTO.getRentalPeriod());
        return poAssembler.toResource(salesService.createPO(customer, poRequestDTO.getPlantId(), period));
    }

    @PostMapping("/orders/{id}/accept")
    public PurchaseOrderDTO acceptPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.acceptPurchaseOrder(id));
    }

    @DeleteMapping("/orders/{id}")
    public PurchaseOrderDTO cancelPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.cancelPurchaseOrder(id));
    }

    @DeleteMapping("/orders/{id}/accept")
    public PurchaseOrderDTO rejectPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.rejectPurchaseOrder(id));
    }

    @GetMapping(value = "/dispatches", params = {"date"})
    public List<PurchaseOrderDTO> fetchDispatches(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws Exception {
        return poAssembler.toResources(salesService.findDispatches(date));
    }

    @PostMapping("/customers")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO partialCustomerDto) throws Exception {
        Customer customer = customerService.createCustomer(partialCustomerDto.getEmail());
        return CustomerDTO.of(customer.getId(), customer.getToken(), customer.getEmail());
    }

    @PostMapping(value = "/orders/:id/dispatch")
    public void dispatchPO(@PathVariable String id) throws PurchaseOrderNotFoundException, POValidationException {
        salesService.dispatchPO(id);
    }

    @PostMapping(value = "/orders/:id/delivery/accept")
    public void acceptDelivery(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        salesService.acceptDelivery(id);
    }

    @PostMapping(value = "/orders/:id/delivery/reject")
    public void rejectDelivery(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        salesService.rejectDelivery(id);
    }

    @PostMapping(value = "/orders/:id/return")
    public void returnPlant(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        salesService.returnPlant(id);
    }

    @ExceptionHandler(POValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlePOValidationException(POValidationException ex) {
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
    }
}
