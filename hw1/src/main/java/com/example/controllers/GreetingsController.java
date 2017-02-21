package com.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by ioane5 on 2/19/17.
 */
@Controller
public class GreetingsController {

    @GetMapping
    public String welcome() {
        return "welcome";
    }
}
