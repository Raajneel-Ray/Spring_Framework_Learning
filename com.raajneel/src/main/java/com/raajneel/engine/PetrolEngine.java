package com.raajneel.engine;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Component;

@Component
public class PetrolEngine implements Engine {
    @PostConstruct
    public void init() {
        System.out.println("Petrol Engine initialized.");
    }
    @Override
    public void start() {
        System.out.println("Petrol engine started");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PetrolEngine destroyed");
    }
}
