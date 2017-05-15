package com.example.sales.rest.controllers;

import com.example.common.application.exceptions.POValidationException;
import com.example.common.application.exceptions.PurchaseOrderNotFoundException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.application.services.PurchaseOrderAssembler;
import com.example.sales.application.services.SalesService;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    private SalesService salesService;

    @Autowired
    private PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    private PurchaseOrderAssembler poAssembler;
    @Autowired
    private PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    private BusinessPeriodAssembler businessPeriodAssembler;

    @GetMapping("/orders/{id}")
    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") String id) throws PurchaseOrderNotFoundException {
        return purchaseOrderAssembler.toResource(salesService.findPO(id));
    }

    @GetMapping("/orders")
    public List<PurchaseOrderDTO> fetchPurchaseOrders() {
        return purchaseOrderAssembler.toResources(salesService.findAllPOs());
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws POValidationException {
        PlantInventoryEntry plant = plantInventoryEntryAssembler.fromResource(partialPODTO.getPlant());
        BusinessPeriod period = businessPeriodAssembler.fromResource(partialPODTO.getRentalPeriod());

        PurchaseOrder purchaseOrder = salesService.createPO(plant, period);
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
    public PurchaseOrderDTO cancelPurchaseOrder(@PathVariable String id) throws Exception {
        PurchaseOrder po = salesService.findPO(id);
        List<POStatus> acceptedStatuses = Arrays.asList(POStatus.PENDING, POStatus.ACCEPTED);
        if(acceptedStatuses.contains(po.getStatus())){
            return poAssembler.toResource(salesService.closePurchaseOrder(id));
        }else{
            throw new POValidationException();
        }
    }

//    @ExceptionHandler(PlantNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public void handlePlantNotFoundException(PlantNotFoundException ex) {
//    }


    @GetMapping(value = "/dispatches", params = {"date"})
    public List<PurchaseOrderDTO> fetchDispatches(@RequestParam(value="date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) throws Exception {
        System.out.println(date);
        List<PurchaseOrder> pos = salesService.findDispatches(date);

        return poAssembler.toResources(pos);
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
