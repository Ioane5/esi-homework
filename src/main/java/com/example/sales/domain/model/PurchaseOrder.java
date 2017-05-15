package com.example.sales.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PurchaseOrder {
    @Id
    String id;

    @OneToOne
    PlantReservation reservation;

    @ManyToOne
    PlantInventoryEntry plant;

    LocalDate issueDate;
    LocalDate paymentSchedule;
    @Column(precision = 8, scale = 2)
    BigDecimal total;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Embedded
    BusinessPeriod rentalPeriod;

    @ManyToOne
    Customer customer;

    public static PurchaseOrder of(String id, Customer customer, PlantInventoryEntry plant, LocalDate issueDate, BusinessPeriod rentalPeriod) {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.plant = plant;
        po.issueDate = issueDate;
        po.rentalPeriod = rentalPeriod;
        po.status = POStatus.PENDING;
        po.customer = customer;
        return po;
    }

    public PurchaseOrder addReservationAndAcceptPO(PlantReservation reservation) {
        this.reservation = reservation;
        this.status = POStatus.ACCEPTED;
        updateTotalCost(this.plant.getPrice());
        return this;
    }

    public void updateTotalCost(BigDecimal price) {
        int numberOfWorkingDays = rentalPeriod.getNumberOfWorkingDays();
        total = price.multiply(BigDecimal.valueOf(numberOfWorkingDays));
    }

    public PurchaseOrder accept() {
        this.status = POStatus.ACCEPTED;
        return this;
    }

    public PurchaseOrder reject() {
        this.status = POStatus.REJECTED;
        return this;
    }

    public PurchaseOrder cancel() {
        this.status = POStatus.CANCELED;
        return this;
    }

    public PurchaseOrder dispatch() {
        this.status = POStatus.PLANT_DISPATCHED;
        return this;
    }

    public PurchaseOrder acceptDelivery() {
        this.status = POStatus.PLANT_DELIVERED;
        return this;
    }

    public PurchaseOrder rejectDelivery() {
        this.status = POStatus.PLANT_REJECTED_BY_CUSTOMER;
        return this;
    }

    public PurchaseOrder returnPlant() {
        this.status = POStatus.PLANT_RETURNED;
        return this;
    }
}
