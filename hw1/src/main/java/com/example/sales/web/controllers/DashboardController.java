package com.example.sales.web.controllers;

import com.example.sales.web.dto.CatalogQueryDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by vkop on 03-Mar-17.
 */


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @GetMapping("/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }
}


