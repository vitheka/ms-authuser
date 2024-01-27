package com.vitor.authuser.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

import static com.vitor.authuser.config.IntegrationTestContainers.MY_SQL_CONTAINER;

@TestConfiguration(proxyBeanMethods = false)
@ImportTestcontainers(IntegrationTestContainers.class)
public class MyTestContainersConfiguration {

    @Bean
    @ServiceConnection
    public MySQLContainer<?> mySqlContainer() {
        return MY_SQL_CONTAINER;
    }
}
