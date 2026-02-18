package com.raajneel.MyApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MyAppApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(MyAppApplication.class, args); // create and run the container

		Dev obj = context.getBean(Dev.class);
//		Dev obj = new Dev();
		obj.build();
	}

}
