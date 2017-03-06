package com.example.maintenance.domain.model;

import com.example.common.domain.model.BusinessPeriod;
import com.example.inventory.domain.model.PlantReservation;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class MaintenanceTask {

    @Id
    String id;

    String description;

    @Enumerated(value = EnumType.STRING)
    TypeOfWork typeOfWork;

    @Column(precision = 8, scale = 2)
    BigDecimal price;

    @Embedded
    BusinessPeriod maintenancePeriod;

    @OneToOne
    PlantReservation plantReservation;

    @ManyToOne
    MaintenancePlan plan;
}
