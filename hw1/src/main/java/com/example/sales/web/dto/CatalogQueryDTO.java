package com.example.sales.web.dto;

import com.example.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

/**
 * Created by vkop on 03-Mar-17.
 */


@Data
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;
}


