package com.example.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ioane5 on 2/20/17.
 */
@Entity
@Data
public class MaintenancePlan {

    @Id
    @GeneratedValue
    Long id;

    Integer yearOfAction;

    @OneToMany
    List<MaintenanceTask> tasks;

    @ManyToOne
    PlantInventoryItem plant;
}
