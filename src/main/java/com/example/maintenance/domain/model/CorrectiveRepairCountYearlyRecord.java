package com.example.maintenance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CorrectiveRepairCountYearlyRecord {
    int year;
    long count;
}
