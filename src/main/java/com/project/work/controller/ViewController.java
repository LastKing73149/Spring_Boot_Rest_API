package com.project.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/admin")
    public String showAdminPage() {
        return "redirect:/user-api.html";
    }
}
