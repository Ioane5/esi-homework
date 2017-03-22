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

    public static PurchaseOrder of(String id, PlantInventoryEntry plant, LocalDate issueDate, BusinessPeriod rentalPeriod) {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.plant = plant;
        po.issueDate = issueDate;
        po.rentalPeriod = rentalPeriod;
        po.status = POStatus.PENDING;

        return po;
    }

    public PurchaseOrder addReservationAndOpenPO(PlantReservation reservation) {
        this.reservation = reservation;
        this.status = POStatus.OPEN;
        updateTotalCost(this.plant.getPrice());
        return this;
    }

    public void updateTotalCost(BigDecimal price) {
        int numberOfWorkingDays = rentalPeriod.getNumberOfWorkingDays();
        total = price.multiply(BigDecimal.valueOf(numberOfWorkingDays));
    }

    public PurchaseOrder accept() {
        this.status = POStatus.OPEN;
        return this;
    }

    public PurchaseOrder reject() {
        this.status = POStatus.REJECTED;
        return this;
    }

    public void close() {
        this.status = POStatus.CLOSED;
    }

}
