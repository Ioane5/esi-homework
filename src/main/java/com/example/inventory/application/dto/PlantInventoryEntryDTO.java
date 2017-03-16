package com.example.inventory.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode
public class PlantInventoryEntryDTO extends ResourceSupport {
    String _id;
    String name;
    String description;
    BigDecimal price;
}
