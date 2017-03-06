package com.example.maintenance.domain.model;

import com.example.inventory.domain.model.PlantInventoryItem;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class MaintenancePlan {

    @Id
    String id;

    Integer yearOfAction;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    List<MaintenanceTask> tasks;

    @ManyToOne
    PlantInventoryItem plant;
}
