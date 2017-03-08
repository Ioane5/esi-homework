package com.example.sales.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.domain.model.BusinessPeriod;
import com.example.common.infrastructure.IdentifierFactory;
import com.example.inventory.application.services.InventoryService;
import com.example.inventory.domain.model.PlantInventoryItem;
import com.example.inventory.domain.model.PlantReservation;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.inventory.domain.repository.PlantReservationRepository;
import com.example.inventory.web.dto.PlantInventoryEntryDTO;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.domain.repository.PurchaseOrderRepository;
import com.example.sales.web.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SalesService {

    @Autowired
    BusinessPeriodAssembler businessPeriodAssembler;
    @Autowired
    PurchaseOrderAssembler poAssembler;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    PlantReservationRepository plantReservationRepository;
    @Autowired
    PurchaseOrderRepository orderRepo;
    @Autowired
    PlantInventoryEntryRepository plantRepo;
    @Autowired
    IdentifierFactory idFactory;

    public PurchaseOrderDTO createPO(PlantInventoryEntryDTO plantDTO, BusinessPeriodDTO periodDTO) {
        BusinessPeriod businessPeriod = businessPeriodAssembler.fromResource(periodDTO);
        PurchaseOrder po = PurchaseOrder.of(
                IdentifierFactory.nextId(),
                plantRepo.findOne(plantDTO.getId()),
                LocalDate.now(),
                businessPeriod
        );
        orderRepo.save(po);

        try {
            PlantInventoryItem freePlant = inventoryService.reservePlantItem(plantDTO.getId(), periodDTO);

            // plant available
            PlantReservation pr = PlantReservation.of(IdentifierFactory.nextId(),
                    businessPeriod,
                    freePlant);
            plantReservationRepository.save(pr);
            po.addReservationAndOpenPO(pr);
        } catch (InventoryService.NoItemFoundException e) {
            po.rejectPO();
        }
        orderRepo.save(po);

        return poAssembler.toResource(po);
    }
}
