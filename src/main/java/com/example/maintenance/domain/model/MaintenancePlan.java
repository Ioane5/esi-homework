package com.example.maintenance.domain.model;

import com.example.inventory.domain.model.PlantInventoryItem;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class MaintenancePlan {

    @Id
    String id;

    Integer yearOfAction;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    List<MaintenanceTask> tasks;

    @ManyToOne
    PlantInventoryItem plant;
}
