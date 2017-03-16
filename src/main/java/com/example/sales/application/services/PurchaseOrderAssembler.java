package com.example.sales.application.services;

import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.rest.controllers.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    @Autowired
    BusinessPeriodAssembler businessPeriodAssembler;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderAssembler() {
        super(SalesRestController.class, PurchaseOrderDTO.class);
    }

    public PurchaseOrderDTO toResource(PurchaseOrder po) {
        PurchaseOrderDTO dto = createResourceWithId(po.getId(), po);
        dto.set_id(po.getId());
        dto.setStatus(po.getStatus().toString());
        dto.setPlant(plantInventoryEntryAssembler.toResource(po.getPlant()));
        dto.setRentalPeriod(businessPeriodAssembler.toResource(po.getRentalPeriod()));
        dto.setTotal(po.getTotal());
        return dto;
    }
}
