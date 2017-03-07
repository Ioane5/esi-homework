package com.example.sales.web.dto;


import com.example.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderDTO {
    String id;
    String plantName;
    String plantDescription;
    BusinessPeriodDTO rentalPeriod;
    BigDecimal total;
    String status;
}
