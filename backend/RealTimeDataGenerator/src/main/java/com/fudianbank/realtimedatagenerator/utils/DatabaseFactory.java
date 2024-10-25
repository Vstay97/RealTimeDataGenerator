package com.fudianbank.realtimedatagenerator.utils;

import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

@Component
public class DatabaseFactory {
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    private static final String OB_DRIVER = "com.oceanbase.jdbc.Driver";
    private static final String PG_DRIVER = "org.postgresql.Driver";
    private static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    private static final Logger log = Logger.getLogger(DatabaseFactory.class.getName());

    public Connection getConnection(String dbType, String url, String user, String password) throws Exception {
        log.info("Attempting to establish database connection - Type: {}, URL: {}, User: {}", dbType, url, user);
        String driver = switch (dbType.toLowerCase()) {
            case "mysql" -> MYSQL_DRIVER;
            case "oracle" -> ORACLE_DRIVER;
            case "pgsql" -> PG_DRIVER;
            case "ob" -> OB_DRIVER;
            case "db2" -> DB2_DRIVER;
            default -> {
                log.error("Unsupported database type: {}", dbType);
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
            }
        };
        
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, password);
            log.info("Successfully established database connection - Type: {}", dbType);
            return connection;
        } catch (Exception e) {
            log.error("Failed to establish database connection - Type: {}, Error: {}", dbType, e.getMessage(), e);
            throw e;
        }
    }
}
