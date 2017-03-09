package com.example.maintenance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
public class CorrectiveRepairCostYearlyRecord {
    int year;
    BigDecimal cost;
}
