package com.example.common.application.services;

import com.example.common.application.dto.BusinessPeriodDTO;
import com.example.common.domain.model.BusinessPeriod;
import org.springframework.stereotype.Service;

@Service
public class BusinessPeriodAssembler {
    public BusinessPeriodDTO toResource(BusinessPeriod period) {
        BusinessPeriodDTO dto = new BusinessPeriodDTO();
        dto.setStartDate(period.getStartDate());
        dto.setEndDate(period.getEndDate());

        return dto;
    }
}
