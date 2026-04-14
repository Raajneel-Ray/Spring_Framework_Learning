package com.example.secureapp.controller;

import com.example.secureapp.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public GreetingController(CustomUserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/greet")
    public String greet(Model model) { //the Model object plays a key role in passing data from the backend (controller) to the frontend (Thymeleaf view)
        // get the authenticated username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // add the username to the model
        model.addAttribute("username", username);
        return "greet";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // return the login.html template
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // Returns the register.html template
    }

    // POST endpoint that handle user registration and auto-login
    @PostMapping("/register")
    public String registerUser(
        @RequestParam String username,
        @RequestParam String password
    ) {
        // register the user by storing details in hashmap
        try{
            userDetailsService.registerUser(username,password);
        } catch (Exception userExistsAlready){
            return "redirect:/register?error";
        }

        // authenticate the user programmatically
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));


        // set the authentication to the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // redirect to the /login endpoint
        return "redirect:/login?success";

    }



}
