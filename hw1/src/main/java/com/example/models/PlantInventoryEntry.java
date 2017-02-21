package com.example.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by ioane5 on 2/19/17.
 */
@Entity
@Data
public class PlantInventoryEntry {
    @Id
    @GeneratedValue
    Long id;

    String name;
    String description;
    @Column(precision = 8, scale = 2)
    BigDecimal price;
}
