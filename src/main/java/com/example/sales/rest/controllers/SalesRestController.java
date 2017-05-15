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

    @PostMapping("/customers")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO partialCustomerDto) throws Exception {
        Customer customer = customerService.createCustomer(partialCustomerDto.getEmail());
        return CustomerDTO.of(customer.getId(), customer.getToken(), customer.getEmail());
    }

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

    //TODO: handle plant not found exception
    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody PurchaseOrderDTO partialPODTO)
            throws POValidationException {
        PlantInventoryEntry plant = plantInventoryEntryAssembler.fromResource(partialPODTO.getPlant());
        BusinessPeriod period = businessPeriodAssembler.fromResource(partialPODTO.getRentalPeriod());
        Customer customer = customerService.retrieveCustomer(token);

        PurchaseOrder purchaseOrder = salesService.createPO(customer, plant, period);
        PurchaseOrderDTO newPoDTO = poAssembler.toResource(purchaseOrder);

        HttpHeaders headers = new HttpHeaders();

        try {
            headers.setLocation(new URI(newPoDTO.getId().getHref()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        HttpStatus status = newPoDTO.getStatus() == POStatus.REJECTED ? HttpStatus.NOT_FOUND : HttpStatus.CREATED;
        return new ResponseEntity<>(newPoDTO, headers, status);
    }

    //TODO: why it returns status as INVOICED?
    @DeleteMapping("/orders/{id}")
    public PurchaseOrderDTO cancelPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.cancelPurchaseOrder(id));
    }

    //TODO: rewrite it to another dto and why it is in sales?
    @GetMapping(value = "/dispatches", params = {"date"})
    public List<PurchaseOrderDTO> fetchDispatches(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws Exception {
        return poAssembler.toResources(salesService.findDispatches(date));
    }

    //TODO: should it be void?
    @PostMapping(value = "/orders/{id}/dispatch")
    public void dispatchPO(@PathVariable String id) throws PurchaseOrderNotFoundException, POValidationException {
        salesService.dispatchPO(id);
    }

    @PostMapping(value = "/orders/{id}/delivery/accept")
    public void acceptDelivery(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        salesService.acceptDelivery(id);
    }

    @PostMapping(value = "/orders/{id}/delivery/reject")
    public void rejectDelivery(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        salesService.rejectDelivery(id);
    }

    @PostMapping(value = "/orders/{id}/return")
    public void returnPlant(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        salesService.returnPlant(id);
    }

    //TODO: revisit exceptions
    @ExceptionHandler(POValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handlePOValidationException(POValidationException ex) {
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handlePurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
    }
}
