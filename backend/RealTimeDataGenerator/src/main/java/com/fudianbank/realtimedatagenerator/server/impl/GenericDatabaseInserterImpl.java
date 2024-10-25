package com.fudianbank.realtimedatagenerator.server.impl;

import com.fudianbank.realtimedatagenerator.config.DatabaseConnectionConfig;
import com.fudianbank.realtimedatagenerator.entity.StudentDO;
import com.fudianbank.realtimedatagenerator.server.DatabaseInserter;
import com.fudianbank.realtimedatagenerator.utils.DatabaseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.sql.ResultSet;

/**
 * @author Charles
 * @Date 2024-09-30 14:33
 * Belongs to Fudian Bank
 */
@Component
@Slf4j
public class GenericDatabaseInserterImpl implements DatabaseInserter {
    private final DatabaseFactory databaseFactory;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final String dbType;
    private final String databaseName;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final AtomicInteger sidCounter;

    @Autowired
    public GenericDatabaseInserterImpl(DatabaseFactory databaseFactory,
                                       DatabaseConnectionConfig config) {
        this.databaseFactory = databaseFactory;
        this.dbType = config.getDbType();
        this.databaseName = config.getDatabaseName();
        this.jdbcUrl = config.getJdbcUrl();
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.sidCounter = config.getSharedCounter(); // 使用共享计数器
    }

    @Override
    public void run() {
        try {
            createTableIfNotExists();
        } catch (SQLException e) {
            log.error("Error creating table for {}: {}", databaseName, e.getMessage());
            return;
        }
        while (running.get()) {
            try (Connection conn = databaseFactory.getConnection(dbType, jdbcUrl, username, password)) {
                int nextSid = sidCounter.incrementAndGet();
                StudentDO studentDO = generateRandomStudentData(nextSid);
                insertData(studentDO, conn);
                Thread.sleep(1000); // 模拟插入间隔
            } catch (Exception e) {
                log.error("Error in database insertion for {}: {}", databaseName, e.getMessage());
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    private void createTableIfNotExists() throws SQLException {
        try (Connection conn = databaseFactory.getConnection(dbType, jdbcUrl, username, password)) {
            String sql;
            switch (dbType.toLowerCase()) {
                case "mysql":
                    sql = """
                        CREATE TABLE IF NOT EXISTS RT_TEST_STUDENT (
                        SID INT NOT NULL,
                        SNAME VARCHAR(10),
                        SAGE INT,
                        SSEX VARCHAR(10),
                        RANDOMCODE INT,
                        PRIMARY KEY (SID)
                        ) ENGINE=InnoDB ROW_FORMAT=COMPRESSED
                        """;
                    break;
                case "ob_mysql":
                    sql = """
                        CREATE TABLE IF NOT EXISTS RT_TEST_STUDENT (
                        SID INT NOT NULL,
                        SNAME VARCHAR(10),
                        SAGE INT,
                        SSEX VARCHAR(10),
                        RANDOMCODE INT,
                        PRIMARY KEY (SID)
                        ) ENGINE=InnoDB ROW_FORMAT=COMPRESSED
                        """;
                    break;
                case "ob_oracle":
                    sql = """
                        CREATE TABLE IF NOT EXISTS RT_TEST_STUDENT (
                        SID NUMBER NOT NULL ENABLE,
                        SNAME VARCHAR2(10),
                        SAGE NUMBER(*,0),
                        SSEX VARCHAR2(10),
                        RANDOMCODE NUMBER,
                        PRIMARY KEY(SID)
                        ) COMPRESS FOR ARCHIVE REPLICA_NUM=2 BLOCK_SIZE = 16384 USE_BLOOM_FILTER = FALSE TABLE_SIZE = 134217728 PCTFREE = 0
                        """;
                    break;
                case "pgsql":
                    sql = """
                        CREATE TABLE IF NOT EXISTS RT_TEST_STUDENT (
                        SID SERIAL PRIMARY KEY,
                        SNAME VARCHAR(10),
                        SAGE INTEGER,
                        SSEX VARCHAR(10),
                        RANDOMCODE INTEGER
                        )
                        """;
                    break;
                case "db2":
                    sql = """
                        CREATE TABLE RT_TEST_STUDENT (
                        SID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
                        SNAME VARCHAR(10),
                        SAGE INTEGER,
                        SSEX VARCHAR(10),
                        RANDOMCODE INTEGER,
                        PRIMARY KEY (SID)
                        )
                        """;
                    break;
                case "oracle":
                    sql = """
                        CREATE TABLE RT_TEST_STUDENT (
                        SID NUMBER(10) NOT NULL,
                        SNAME VARCHAR2(10),
                        SAGE NUMBER(3),
                        SSEX VARCHAR2(10),
                        RANDOMCODE NUMBER(5),
                        CONSTRAINT RT_TEST_STUDENT_PK PRIMARY KEY (SID)
                        )
                        """;
                    break;
                default:
                    throw new SQLException("Unsupported database type: " + dbType);
            }

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Table RT_TEST_STUDENT created or already exists in " + databaseName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private StudentDO generateRandomStudentData(int sid) {
        return new StudentDO(sid);
    }

    private void insertData(StudentDO studentDO, Connection conn) throws SQLException {
        String sql = "INSERT INTO RT_TEST_STUDENT (SID, SNAME, SAGE, SSEX, RANDOMCODE) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, studentDO.getSid());
            preparedStatement.setString(2, studentDO.getSname());
            preparedStatement.setInt(3, studentDO.getSage());
            preparedStatement.setString(4, studentDO.getSsex());
            preparedStatement.setInt(5, studentDO.getRandomCode());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insertion successful into " + databaseName + ", SID: " + studentDO.getSid());
            } else {
                System.out.println("Insertion failed into " + databaseName);
            }
        }
    }

    @Override
    public void stopInsertion() {
        running.set(false);
    }

    private int getMaxSidFromDatabase() {
        int maxSid = 0;
        String sql = "SELECT COALESCE(MAX(SID), 0) FROM RT_TEST_STUDENT";
        try (Connection conn = databaseFactory.getConnection(dbType, jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                maxSid = rs.getInt(1);
            }
        } catch (Exception e) {
            log.error("Error getting max SID from database: {}", e.getMessage());
        }
        log.info("Max SID from database: {}", maxSid);
        return maxSid;
    }
}
