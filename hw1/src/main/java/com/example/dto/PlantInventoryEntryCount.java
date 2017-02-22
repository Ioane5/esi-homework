package com.example.dto;

import com.example.models.PlantInventoryEntry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Created by ioane5 on 2/22/17.
 */
@Data
@AllArgsConstructor
@ToString
public class PlantInventoryEntryCount {

    PlantInventoryEntry item;
    Long count;
}
