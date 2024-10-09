package com.fudianbank.realtimedatagenerator.utils;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class DatabaseFactory {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String OB_DRIVER = "com.oceanbase.jdbc.Driver";
    private static final String PG_DRIVER = "org.postgresql.Driver";
    private static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";

    public Connection getConnection(String dbType, String url, String user, String password) throws Exception {
        String driver = switch (dbType.toLowerCase()) {
            case "mysql" -> MYSQL_DRIVER;
            case "oracle" -> ORACLE_DRIVER;
            case "pgsql" -> PG_DRIVER;
            case "ob" -> OB_DRIVER;
            case "db2" -> DB2_DRIVER;
            default -> throw new IllegalArgumentException("Unsupported database type: " + dbType);
        };
        
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }
}