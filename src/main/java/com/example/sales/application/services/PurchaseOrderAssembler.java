package com.example.sales.application.services;

import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.rest.controllers.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;


import com.example.common.rest.ExtendedLink;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;


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
        dto.setStatus(po.getStatus());
        dto.setPlant(plantInventoryEntryAssembler.toResource(po.getPlant()));
        dto.setRentalPeriod(businessPeriodAssembler.toResource(po.getRentalPeriod()));
        dto.setTotal(po.getTotal());

        try {
            switch (dto.getStatus()) {
                case PENDING:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .acceptPurchaseOrder(dto.get_id())).toString(),
                            "accept", POST));
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .rejectPurchaseOrder(dto.get_id())).toString(),
                            "reject", DELETE));
                    break;
                case OPEN:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .closePurchaseOrder(dto.get_id())).toString(),
                            "close", DELETE));
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return dto;
    }
}
