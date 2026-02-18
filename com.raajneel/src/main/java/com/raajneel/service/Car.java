package com.raajneel.service;

import com.raajneel.engine.Engine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Car {
    private final Engine engine;
    // it is qualifying the petrol engine over the diesel engine as it both implements the interface Engine
    public Car(@Qualifier("petrolEngine") Engine engine) {
        this.engine = engine;
    }
    public void drive() {
        engine.start();
        System.out.println("Car is driving.....");
    }
}
