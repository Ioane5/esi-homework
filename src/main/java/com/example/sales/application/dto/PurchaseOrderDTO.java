package com.example.sales.application.dto;


import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.rest.ExtendedResourceSupport;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.sales.domain.model.POStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderDTO extends ExtendedResourceSupport {
    String _id;
    PlantInventoryEntryDTO plant;
    BusinessPeriodDTO rentalPeriod;
    BigDecimal total;
    POStatus status;
}
