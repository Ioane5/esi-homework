package com.example.inventory.application.services;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.web.dto.PlantInventoryEntryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantInventoryEntryAssembler {
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plant) {
        PlantInventoryEntryDTO dto = new PlantInventoryEntryDTO();
        dto.setId(plant.getId());
        dto.setDescription(plant.getDescription());
        dto.setName(plant.getName());
        dto.setPrice(plant.getPrice());
        return dto;
    }

    public List<PlantInventoryEntryDTO> toResources(List<PlantInventoryEntry> plants) {
        return plants.stream().map(p -> toResource(p)).collect(Collectors.toList());
    }

    public PlantInventoryEntry fromResource(PlantInventoryEntryDTO dto) {
        return PlantInventoryEntry.of(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice());
    }

}
