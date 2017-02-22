package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by vkop on 22-Feb-17.
 */
@Data
@AllArgsConstructor
@ToString
public class CorrectiveRepairCountYearlyRecord {
    int year;
    long count;
}
