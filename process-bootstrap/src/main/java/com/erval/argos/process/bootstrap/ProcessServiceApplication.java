package com.erval.argos.process.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.erval.argos")
public class ProcessServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessServiceApplication.class, args);
    }
}
