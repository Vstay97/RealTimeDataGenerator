package com.fudianbank.realtimedatagenerator.server;

import com.fudianbank.realtimedatagenerator.config.DatabaseConnectionConfig;
import com.fudianbank.realtimedatagenerator.server.DatabaseInserter;
import com.fudianbank.realtimedatagenerator.server.impl.GenericDatabaseInserterImpl;
import com.fudianbank.realtimedatagenerator.utils.DatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Charles
 * @Date 2024-09-30 14:35
 * Belongs to Fudian Bank
 */
@Component
public class InsertDataServer {

    private final ConcurrentHashMap<String, DatabaseInserter> inserters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Thread> threads = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> sidCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, TaskInfo> runningTasks = new ConcurrentHashMap<>();

    @Autowired
    private DatabaseFactory databaseFactory;

    public void startInsertion(String dbType, String databaseName, int threadCount, String jdbcUrl, String username, String password) {
        for (int i = 0; i < threadCount; i++) {
            String key = getKey(dbType, databaseName, i);
            DatabaseConnectionConfig config = new DatabaseConnectionConfig();
            config.setDbType(dbType);
            config.setDatabaseName(databaseName);
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            DatabaseInserter inserter = new GenericDatabaseInserterImpl(databaseFactory, config);
            inserters.put(key, inserter);
            Thread thread = new Thread(inserter);
            threads.put(key, thread);
            thread.start();
            System.out.println("Started insertion for " + key);
        }
        String taskKey = dbType + "-" + databaseName;
        runningTasks.put(taskKey, new TaskInfo(taskKey, dbType, threadCount));
    }

    private int getMaxSidFromDatabase(String dbType, String jdbcUrl, String username, String password) {
        int maxSid = 0;
        String sql = "SELECT COALESCE(MAX(SID), 0) FROM RT_TEST_STUDENT";
        try (Connection conn = databaseFactory.getConnection(dbType, jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                maxSid = rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("Error getting max SID from database: " + e.getMessage());
        }
        System.out.println("Max SID from database: " + maxSid);
        return maxSid;
    }

    public void stopInsertion(String dbType, String databaseName) {
        for (int i = 0; ; i++) {
            String key = getKey(dbType, databaseName, i);
            DatabaseInserter inserter = inserters.remove(key);
            if (inserter == null) {
                break; // No more threads for this database
            }
            inserter.stopInsertion();
            Thread thread = threads.remove(key);
            if (thread != null) {
                try {
                    thread.join(5000); // 等待线程结束，最多等待5秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Stopped insertion for " + key);
        }
        sidCounters.remove(dbType + "-" + databaseName);
        String taskKey = dbType + "-" + databaseName;
        runningTasks.remove(taskKey);
    }

    public List<TaskInfo> getRunningTasks() {
        return new ArrayList<>(runningTasks.values());
    }

    private String getKey(String dbType, String databaseName, int threadIndex) {
        return dbType + "-" + databaseName + "-" + threadIndex;
    }

    public static class TaskInfo {
        private String taskName;
        private String taskType;
        private int threadCount;

        public TaskInfo(String taskName, String taskType, int threadCount) {
            this.taskName = taskName;
            this.taskType = taskType;
            this.threadCount = threadCount;
        }

        // Getters
        public String getTaskName() { return taskName; }
        public String getTaskType() { return taskType; }
        public int getThreadCount() { return threadCount; }
    }
}
