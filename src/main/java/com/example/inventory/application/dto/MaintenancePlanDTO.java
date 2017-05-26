package com.example.inventory.application.dto;


import com.example.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

@Data
public class MaintenancePlanDTO {
    String id;
    String plantId;
    BusinessPeriodDTO maintenancePeriod;
}
