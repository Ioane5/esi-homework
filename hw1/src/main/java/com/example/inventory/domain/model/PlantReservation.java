package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.maintenance.domain.model.MaintenancePlan;
import com.example.sales.domain.model.PurchaseOrder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PlantReservation {
    @Id
    String id;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    PlantInventoryItem plant;

    @ManyToOne
    MaintenancePlan maintenancePlan;

    @ManyToOne
    PurchaseOrder rental;

    public static PlantReservation of(String id, BusinessPeriod schedule, PlantInventoryItem plant, MaintenancePlan maintenancePlan) {
        PlantReservation r = new PlantReservation();
        r.id = id;
        r.schedule = schedule;
        r.plant = plant;
        r.maintenancePlan = maintenancePlan;

        return r;
    }


    public static PlantReservation of(String id, BusinessPeriod schedule, PlantInventoryItem plant, PurchaseOrder rental) {
        PlantReservation r = new PlantReservation();
        r.id = id;
        r.schedule = schedule;
        r.plant = plant;
        r.rental = rental;

        return r;
    }
}
