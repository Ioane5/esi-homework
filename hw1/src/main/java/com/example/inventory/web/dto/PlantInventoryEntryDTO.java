package com.example.inventory.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class PlantInventoryEntryDTO {
    String id;
    String name;
    String description;
    BigDecimal price;
}
