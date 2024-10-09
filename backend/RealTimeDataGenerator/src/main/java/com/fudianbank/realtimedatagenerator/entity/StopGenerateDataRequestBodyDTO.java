package com.fudianbank.realtimedatagenerator.entity;

import lombok.Data;

/**
 * @author Charles
 * @Date 2024-09-30 16:39
 * Belongs to Fudian Bank
 */
@Data
public class StopGenerateDataRequestBodyDTO {
    private String dbType;
    private String databaseName;
}
