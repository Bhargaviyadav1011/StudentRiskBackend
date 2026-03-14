package com.academic.risk.backend.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

@Configuration
public class DatabaseConfig {
    private static final String DEFAULT_LOCAL_MYSQL_URL =
            "jdbc:mysql://localhost:3306/academic_risk_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DEFAULT_LOCAL_MYSQL_USERNAME = "root";
    private static final String DEFAULT_LOCAL_MYSQL_PASSWORD = "root";

    @Bean
    @Primary
    public DataSource dataSource(Environment environment) {
        String rawUrl = firstNonBlank(
                environment.getProperty("SPRING_DATASOURCE_URL"),
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("JDBC_DATABASE_URL"),
                environment.getProperty("spring.datasource.url")
        );

        String username = firstNonBlank(
                environment.getProperty("SPRING_DATASOURCE_USERNAME"),
                environment.getProperty("DATABASE_USERNAME"),
                environment.getProperty("spring.datasource.username"),
                DEFAULT_LOCAL_MYSQL_USERNAME
        );

        String password = firstNonBlank(
                environment.getProperty("SPRING_DATASOURCE_PASSWORD"),
                environment.getProperty("DATABASE_PASSWORD"),
                environment.getProperty("spring.datasource.password"),
                DEFAULT_LOCAL_MYSQL_PASSWORD
        );

        String jdbcUrl = normalizeJdbcUrl(rawUrl);
        String driverClassName = jdbcUrl.startsWith("jdbc:postgresql:")
                ? "org.postgresql.Driver"
                : "com.mysql.cj.jdbc.Driver";

        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }

    private String normalizeJdbcUrl(String rawUrl) {
        if (isBlank(rawUrl) || rawUrl.startsWith("${")) {
            return DEFAULT_LOCAL_MYSQL_URL;
        }

        String trimmedUrl = rawUrl.trim();

        if (trimmedUrl.startsWith("http:postgresql://")) {
            trimmedUrl = "jdbc:" + trimmedUrl.substring("http:".length());
        } else if (trimmedUrl.startsWith("postgresql://")) {
            trimmedUrl = "jdbc:" + trimmedUrl;
        } else if (trimmedUrl.startsWith("postgres://")) {
            trimmedUrl = "jdbc:postgresql://" + trimmedUrl.substring("postgres://".length());
        }

        return trimmedUrl;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (!isBlank(value)) {
                return value.trim();
            }
        }
        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
