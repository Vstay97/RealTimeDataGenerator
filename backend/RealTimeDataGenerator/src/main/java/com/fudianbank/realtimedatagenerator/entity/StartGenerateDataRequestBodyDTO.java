package com.fudianbank.realtimedatagenerator.entity;

import lombok.Data;

/**
 * @author Charles
 * @Date 2024-09-30 15:02
 * Belongs to Fudian Bank
 */
@Data
public class StartGenerateDataRequestBodyDTO {
    private String dbType;
    private String databaseName;
    private int threadCount;
    private String jdbcUrl;
    private String username;
    private String password;
}
