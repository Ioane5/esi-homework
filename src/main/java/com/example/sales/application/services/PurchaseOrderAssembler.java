package com.example.sales.application.services;

import com.example.common.application.services.BusinessPeriodAssembler;
import com.example.common.rest.ExtendedLink;
import com.example.inventory.application.services.PlantInventoryEntryAssembler;
import com.example.sales.application.dto.PurchaseOrderDTO;
import com.example.sales.domain.model.PurchaseOrder;
import com.example.sales.rest.controllers.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.*;


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
            dto.add(new ExtendedLink(
                    linkTo(methodOn(SalesRestController.class)
                            .fetchPurchaseOrder(null, dto.get_id())).toString(),
                    "self", GET));

            switch (dto.getStatus()) {
                case ACCEPTED:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .cancelPurchaseOrder(dto.get_id())).toString(),
                            "cancel", DELETE));
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .dispatchPO(dto.get_id())).toString(),
                            "dispatch", POST));
                    break;
                case PLANT_DISPATCHED:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .acceptDelivery(dto.get_id())).toString(),
                            "acceptDelivery", POST));
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .rejectDelivery(dto.get_id())).toString(),
                            "rejectDelivery", POST));
                    break;
                case PLANT_DELIVERED:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(SalesRestController.class)
                                    .returnPlant(dto.get_id())).toString(),
                            "return", POST));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return dto;
    }
}
