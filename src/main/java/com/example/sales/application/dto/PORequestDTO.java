package com.example.sales.application.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

@Data
public class PORequestDTO {
    String plantId;
    String constructionSite;
    BusinessPeriodDTO rentalPeriod;
}
