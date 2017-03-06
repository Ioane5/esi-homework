package com.example.inventory.domain.model;

import com.example.inventory.domain.model.EquipmentCondition;
import com.example.inventory.domain.model.PlantInventoryEntry;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryItem {
    @Id
    @GeneratedValue
    Long id;
    String serialNumber;

    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;
    @ManyToOne
    PlantInventoryEntry plantInfo;
}
