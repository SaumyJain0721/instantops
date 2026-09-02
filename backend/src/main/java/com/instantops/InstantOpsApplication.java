package com.instantops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InstantOpsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstantOpsApplication.class, args);
    }
}
