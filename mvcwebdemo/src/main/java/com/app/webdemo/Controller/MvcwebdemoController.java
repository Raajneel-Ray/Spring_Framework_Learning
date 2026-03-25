package com.app.webdemo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcwebdemoController {

    // Serve the index.html page
    @GetMapping("/")
    public String home() {
        return "index"; // Refers to src/main/resources/templates/index.html
    }

    // Serve the registration.html page
    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }
}