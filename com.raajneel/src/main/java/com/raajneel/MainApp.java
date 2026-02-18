package com.raajneel;

import com.raajneel.config.AppConfig;
import com.raajneel.service.Car;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(AppConfig.class);
        //System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
        Car car = context.getBean(Car.class);
        car.drive();

        context.close();
    }
}
