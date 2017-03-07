package com.example.inventory.web.controllers;

import com.example.inventory.domain.model.PlantInventoryEntry;
import com.example.inventory.domain.repository.PlantInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by ioane5 on 2/19/17.
 */
@Controller
public class PlantInventoryEntryController {
    @Autowired
    PlantInventoryEntryRepository repo;

    @GetMapping("/plants")
    public String list(Model model) {
        model.addAttribute("plants", repo.findAll());
        return "plants/list";
    }

    @GetMapping(value = "/plants/form")
    public String form(Model model) {
//        todo do we need this? fix it later
//        model.addAttribute("plant", new PlantInventoryEntry());
        return "plants/create";
    }

    @PostMapping(value = "/plants")
    public String create(PlantInventoryEntry plant) {
        repo.save(plant);
        return "redirect:/plants";
    }
}
