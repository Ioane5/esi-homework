package com.example.sales.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.model.PlantReservation;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        return this;
    }


}
