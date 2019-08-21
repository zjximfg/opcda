package com.imes.opcda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OpcdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpcdaApplication.class, args);
    }

}

