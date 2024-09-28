package io.bootify.ecommerce_project.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.ecommerce_project.domain")
@EnableJpaRepositories("io.bootify.ecommerce_project.repos")
@EnableTransactionManagement
public class DomainConfig {
}
