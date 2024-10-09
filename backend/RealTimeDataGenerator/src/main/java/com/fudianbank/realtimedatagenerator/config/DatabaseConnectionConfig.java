package com.fudianbank.realtimedatagenerator.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Charles
 * @Date 2024-10-08 09:50
 * Belongs to Fudian Bank
 */
@Data
@Component
public class DatabaseConnectionConfig {
    private String dbType;
    private String databaseName;
    private String jdbcUrl;
    private String username;
    private String password;

    // Getters for all fields
}
