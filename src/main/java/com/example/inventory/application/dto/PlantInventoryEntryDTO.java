package com.example.inventory.application.dto;

import com.example.common.rest.ExtendedResourceSupport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class PlantInventoryEntryDTO extends ExtendedResourceSupport {
    String _id;
    String name;
    String description;
    BigDecimal price;
}
