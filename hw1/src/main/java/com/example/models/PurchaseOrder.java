package com.example.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
@Entity
@Data
public class PurchaseOrder {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany
    List<PlantReservation> reservations;
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
}
