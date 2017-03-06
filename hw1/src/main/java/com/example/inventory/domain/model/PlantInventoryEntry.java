package com.example.inventory.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryEntry {
    @Id
    String id;

    String name;
    String description;
    @Column(precision = 8, scale = 2)
    BigDecimal price;
}
