package com.example.sales.web.dto;


import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.web.dto.PlantInventoryEntryDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderDTO {
    String id;
    PlantInventoryEntryDTO plant;
    BusinessPeriodDTO rentalPeriod;
    BigDecimal total;
    String status;
}
