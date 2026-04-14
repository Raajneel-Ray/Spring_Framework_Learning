package com.example.secureapp.controller;

import com.example.secureapp.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
}
