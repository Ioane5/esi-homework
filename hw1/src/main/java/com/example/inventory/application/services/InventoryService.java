package com.example.inventory.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private BusinessPeriodAssembler businessPeriodAssembler;

    public PlantInventoryItem reservePlantItem(String entryId, BusinessPeriodDTO period) throws NoItemFoundException {
        BusinessPeriod bp = businessPeriodAssembler.fromResource(period);
        List<PlantInventoryItem> items = inventoryRepo.findAvailablePlantsInBusinessPeriod(entryId, bp);
        if (items.size() < 1) {
            throw new NoItemFoundException();
        }
        return items.get(0);
    }

    public static class NoItemFoundException extends Exception {

    }
}
