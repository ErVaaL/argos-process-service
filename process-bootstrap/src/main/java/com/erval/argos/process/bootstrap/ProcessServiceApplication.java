package com.erval.argos.process.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.erval.argos")
@EnableMongoRepositories(basePackages = "com.erval.argos.process.adapters.mongo.repositories")
public class ProcessServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessServiceApplication.class, args);
    }
}
