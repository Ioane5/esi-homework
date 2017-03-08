package com.example.sales.application.services;

import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.web.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderAssembler {
    @Autowired
    BusinessPeriodAssembler businessPeriodAssembler;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderDTO toResource(PurchaseOrder po) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setStatus(po.getStatus().toString());
        dto.setId(po.getId());
        dto.setPlant(plantInventoryEntryAssembler.toResource(po.getPlant()));
        dto.setRentalPeriod(businessPeriodAssembler.toResource(po.getRentalPeriod()));
        dto.setTotal(po.getTotal());
        return dto;
    }
}
