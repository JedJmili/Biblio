package com.bibliotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BiblioTechApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiblioTechApplication.class, args);
    }
}
