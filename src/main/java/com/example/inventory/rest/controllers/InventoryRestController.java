package com.example.inventory.rest.controllers;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.MaintenanceReservationDTO;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.inventory.domain.model.PlantInventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryRestController {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    private BusinessPeriodAssembler businessPeriodAssembler;

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name") String plantName,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<PlantInventoryEntry> plants = inventoryService.findAvailablePlants(plantName, BusinessPeriod.of(startDate, endDate));
        return plantInventoryEntryAssembler.toResources(plants);
    }

    @PostMapping("/reservations")
    public void reservePlantForMaintenance(@RequestBody MaintenanceReservationDTO reservationDTO) throws PlantNotFoundException {
        String itemId = reservationDTO.getItemId();
        String maintenancePlanId = reservationDTO.getMaintenancePlanId();
        BusinessPeriod maintenancePeriod = businessPeriodAssembler.fromResource(reservationDTO.getMaintenancePeriod());
        inventoryService.reservePlantItem(itemId, maintenancePeriod, maintenancePlanId);
        // TODO: Should discard any existing reservations on the item with given itemId
        // TODO: Should find replacement items if possible
        // TODO: Should email client if replacement was not found (reject po)
    }
}
