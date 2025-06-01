package com.example.webapp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.example.product.entity"})
@EnableJpaRepositories(basePackages = {"com.example.product.repository"})
public class JpaConfig {
    // This configuration enables JPA repositories from the webapp module
    // and scans for entities in the webapp module
}
