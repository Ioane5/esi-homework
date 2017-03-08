package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.maintenance.domain.model.MaintenancePlan;
import com.example.sales.domain.model.PurchaseOrder;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PlantReservation {
    @Id
    String id;

    @Embedded
    BusinessPeriod schedule;

    @ManyToOne
    PlantInventoryItem plant;

    @ManyToOne
    MaintenancePlan maintenancePlan;

    @OneToOne
    PurchaseOrder rental;

    public static PlantReservation of(String id, BusinessPeriod schedule, PlantInventoryItem plant) {
        PlantReservation r = new PlantReservation();
        r.id = id;
        r.schedule = schedule;
        r.plant = plant;

        return r;
    }

    public PlantReservation setMaintenance(MaintenancePlan maintenancePlan) {
        this.maintenancePlan = maintenancePlan;
        return this;
    }

    public PlantReservation setPurchaseOrder(PurchaseOrder po) {
        this.rental = po;
        return this;
    }
}