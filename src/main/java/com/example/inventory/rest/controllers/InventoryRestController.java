package com.example.inventory.rest.controllers;

import com.example.common.application.exceptions.PlantNotFoundException;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.application.dto.MaintenancePlanDTO;
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
@CrossOrigin()
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


    @GetMapping("/plants/all")
    public List<PlantInventoryEntryDTO> findAllPlants() {
        List<PlantInventoryEntry> plants = inventoryService.findAll();
        return plantInventoryEntryAssembler.toResources(plants);
    }


    @GetMapping("/plants/{id}")
    public PlantInventoryEntryDTO findAvailablePlant(
            @PathVariable("id") String id,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        PlantInventoryEntry plant = inventoryService.findAvailablePlant(id, BusinessPeriod.of(startDate, endDate));
        return plantInventoryEntryAssembler.toResource(plant);
    }

    @PostMapping("/reservations")
    public void reservePlantForMaintenance(@RequestBody MaintenancePlanDTO reservationDTO) throws PlantNotFoundException {
        String plantId = reservationDTO.getPlantId();
        String maintenancePlanId = reservationDTO.getId();
        BusinessPeriod maintenancePeriod = businessPeriodAssembler.fromResource(reservationDTO.getMaintenancePeriod());
        inventoryService.reservePlantItem(plantId, maintenancePeriod, maintenancePlanId);
    }
}
