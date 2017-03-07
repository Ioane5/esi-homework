package com.example.sales.web.dto;


import com.example.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

@Data
public class PurchaseOrderDTO {
    String plantName;
    String plantDescription;
    BusinessPeriodDTO rentalPeriod;
    String total;
    String status;
}
