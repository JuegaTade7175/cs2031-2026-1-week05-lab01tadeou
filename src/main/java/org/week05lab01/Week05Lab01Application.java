package org.week05lab01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Week05Lab01Application {
    public static void main(String[] args) {
        SpringApplication.run(Week05Lab01Application.class, args);
    }
}
