package com.example.models;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by ioane5 on 2/20/17.
 */
@Entity
@Data
public class PlantReservation {
    @Id
    @GeneratedValue
    Long id;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    PlantInventoryItem plant;

    @ManyToOne
    MaintenancePlan maintenancePlan;

    @ManyToOne
    PurchaseOrder rental;
}
