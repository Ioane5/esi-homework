package com.example.inventory.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.sales.domain.model.PurchaseOrder;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    String maintenancePlanId;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    PurchaseOrder rental;

    public static PlantReservation of(String id, BusinessPeriod schedule, PlantInventoryItem plant) {
        PlantReservation r = new PlantReservation();
        r.id = id;
        r.schedule = schedule;
        r.plant = plant;

        return r;
    }

    public void setSchedule(BusinessPeriod schedule) {
        this.schedule = schedule;
    }

    public PlantReservation withMaintenancePlanId(String maintenancePlanId) {
        this.maintenancePlanId = maintenancePlanId;
        return this;
    }

    public PlantReservation withPurchaseOrder(PurchaseOrder po) {
        this.rental = po;
        return this;
    }
}
