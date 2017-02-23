package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by vkop on 23-Feb-17.
 */
@Data
@AllArgsConstructor
@ToString
public class CorrectiveRepairCostYearlyRecord {
    int year;
    BigDecimal cost;
}
