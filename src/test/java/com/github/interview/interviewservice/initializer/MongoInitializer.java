package com.github.interview.interviewservice.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MongoDBContainer;

import static org.springframework.boot.test.util.TestPropertyValues.of;

public class MongoInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    public static final int MONGO_PORT = 27017;
    public static final String MONGO_PROPERTIES_TEMPLATE = "spring.data.mongodb.host=%s:%d";
    private final MongoDBContainer mongoDBContainer = new MongoDBContainer();

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        mongoDBContainer.start();

        String containerIpAddress = mongoDBContainer.getContainerIpAddress();
        Integer mappedPort = mongoDBContainer.getMappedPort(MONGO_PORT);
        of(String.format(MONGO_PROPERTIES_TEMPLATE, containerIpAddress, mappedPort), "spring.data.mongodb.database=test")
                .applyTo(applicationContext.getEnvironment());
    }
}
