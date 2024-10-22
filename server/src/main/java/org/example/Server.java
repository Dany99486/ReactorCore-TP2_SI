package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("Starting server...");
        // log database environment variables
        logger.info("POSTGRES_DB: " + System.getenv("POSTGRES_DB"));
        logger.info("POSTGRES_USER: " + System.getenv("POSTGRES_USER"));
        logger.info("POSTGRES_PASSWORD: " + System.getenv("POSTGRES_PASSWORD"));

        SpringApplication.run(Server.class, args);
    }
}

