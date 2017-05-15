package com.example.sales.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.inventory.application.dto.PlantInventoryEntryDTO;
import lombok.Data;

@Data
public class PORequestDTO {
    PlantInventoryEntryDTO plant;
    BusinessPeriodDTO rentalPeriod;
    String constructionSite;
}
