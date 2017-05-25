package com.example.sales.rest.controllers;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.dto.ExceptionDTO;
import com.example.common.application.exceptions.CustomerNotFoundException;
import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.exceptions.UniqueCustomerViolationException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    private SalesService salesService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PurchaseOrderAssembler poAssembler;
    @Autowired
    private BusinessPeriodAssembler businessPeriodAssembler;

    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO partialCustomerDto) throws UniqueCustomerViolationException {
        Customer customer = customerService.createCustomer(partialCustomerDto.getEmail());
        CustomerDTO customerDTO = CustomerDTO.of(customer.getId(), customer.getToken(), customer.getEmail());

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(customerDTO, headers, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{id}")
    public PurchaseOrderDTO fetchPurchaseOrder(@RequestHeader("Authorization") String token,
                                               @PathVariable("id") String id) throws PurchaseOrderNotFoundException, CustomerNotFoundException {
        Customer customer = customerService.retrieveCustomer(token);
        return poAssembler.toResource(salesService.findPO(id, customer));
    }

    @GetMapping("/orders")
    public List<PurchaseOrderDTO> fetchPurchaseOrders(@RequestHeader("Authorization") String token) throws CustomerNotFoundException {
        Customer customer = customerService.retrieveCustomer(token);
        return poAssembler.toResources(salesService.findAllPOs(customer));
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestHeader("Authorization") String token, @RequestBody PORequestDTO poRequestDTO) throws POValidationException, CustomerNotFoundException {
        Customer customer = customerService.retrieveCustomer(token);
        BusinessPeriod period = businessPeriodAssembler.fromResource(poRequestDTO.getRentalPeriod());
        PurchaseOrderDTO poDTO = poAssembler.toResource(salesService.createPO(customer, poRequestDTO.getPlantId(), period));
        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<>(poDTO, headers, convertToHttpStatus(poDTO.getStatus()));
    }

    private HttpStatus convertToHttpStatus(POStatus status) {
        switch (status) {
            case REJECTED:
                return HttpStatus.NOT_FOUND;
            default:
                return HttpStatus.CREATED;
        }
    }

    @DeleteMapping("/orders/{id}")
    public PurchaseOrderDTO cancelPurchaseOrder(@PathVariable String id) throws Exception {
        return poAssembler.toResource(salesService.cancelPurchaseOrder(id));
    }

    @GetMapping(value = "/orders/dispatches", params = {"date"})
    public List<PurchaseOrderDTO> fetchDispatches(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws Exception {
        return poAssembler.toResources(salesService.findDispatches(date));
    }

    @GetMapping(value = "/arrivals", params = {"date"})
    public List<PurchaseOrderDTO> fetchArrivals(
            @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws Exception {
        return poAssembler.toResources(salesService.findArrivals(date));
    }

    @PostMapping(value = "/orders/{id}/dispatch")
    public PurchaseOrderDTO dispatchPO(@PathVariable String id) throws PurchaseOrderNotFoundException, POValidationException {
        return poAssembler.toResource(salesService.dispatchPO(id));
    }

    @PostMapping(value = "/orders/{id}/delivery/accept")
    public PurchaseOrderDTO acceptDelivery(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        return poAssembler.toResource(salesService.acceptDelivery(id));
    }

    @PostMapping(value = "/orders/{id}/delivery/reject")
    public PurchaseOrderDTO rejectDelivery(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        return poAssembler.toResource(salesService.rejectDelivery(id));
    }

    @PostMapping(value = "/orders/{id}/return")
    public PurchaseOrderDTO returnPlant(@PathVariable String id) throws POValidationException, PurchaseOrderNotFoundException {
        return poAssembler.toResource(salesService.returnPlant(id));
    }

    @PutMapping(value = "/orders/{id}")
    public ResponseEntity<PurchaseOrderDTO> resubmitPO(@PathVariable String id, @RequestBody BusinessPeriodDTO newPeriodDTO) throws POValidationException, PurchaseOrderNotFoundException {
        BusinessPeriod newPeriod = businessPeriodAssembler.fromResource(newPeriodDTO);
        PurchaseOrderDTO purchaseOrderDTO = poAssembler.toResource(salesService.resubmitPO(id, newPeriod));

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(purchaseOrderDTO, headers, HttpStatus.CREATED);
    }

    @ExceptionHandler(POValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handlePOValidationException(POValidationException ex) {
        return handleException(ex);
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handlePurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
        return handleException(ex);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDTO handleCustomerNotFoundException(CustomerNotFoundException ex) {
        return handleException(ex);
    }

    @ExceptionHandler(UniqueCustomerViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDTO handleUniqueCustomerViolationException(UniqueCustomerViolationException ex) {
        return handleException(ex);
    }

    private ExceptionDTO handleException(Exception ex) {
        return ExceptionDTO.of(ex.getMessage());
    }
}
