package com.fudianbank.realtimedatagenerator.controller;

import com.fudianbank.realtimedatagenerator.entity.StartGenerateDataRequestBodyDTO;
import com.fudianbank.realtimedatagenerator.entity.StopGenerateDataRequestBodyDTO;
import com.fudianbank.realtimedatagenerator.server.InsertDataServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Charles
 * @Date 2024-09-24 14:27
 * Belongs to Fudian Bank
 */
@Slf4j
@RestController
@RequestMapping("/generator")
public class HttpServerApi {
    @Autowired
    private InsertDataServer insertDataServer;

    @GetMapping("/")
    public String HelloWorld(){
        log.info("Received request to HelloWorld endpoint");
        return "Hello World";
    }

    @PostMapping ("start")
    public String startGeneratorData(@RequestBody StartGenerateDataRequestBodyDTO startGenerateDataRequestBodyDTO){
        log.info("Starting data generation with parameters: {}", startGenerateDataRequestBodyDTO);
        try {
            insertDataServer.startInsertion(
                startGenerateDataRequestBodyDTO.getDbType(), 
                startGenerateDataRequestBodyDTO.getDatabaseName(), 
                startGenerateDataRequestBodyDTO.getThreadCount(), 
                startGenerateDataRequestBodyDTO.getJdbcUrl(), 
                startGenerateDataRequestBodyDTO.getUsername(), 
                startGenerateDataRequestBodyDTO.getPassword()
            );
            log.info("Successfully started data generation for database: {}", startGenerateDataRequestBodyDTO.getDatabaseName());
            return "success";
        } catch (Exception e) {
            log.error("Failed to start data generation: {}", e.getMessage(), e);
            return "error: " + e.getMessage();
        }
    }

    @PostMapping ("stop")
    public String stopGeneratorData(@RequestBody StopGenerateDataRequestBodyDTO stopGenerateDataRequestBodyDTO){
        log.info("Stopping data generation for database: {}", stopGenerateDataRequestBodyDTO);
        try {
            insertDataServer.stopInsertion(
                stopGenerateDataRequestBodyDTO.getDbType(), 
                stopGenerateDataRequestBodyDTO.getDatabaseName()
            );
            log.info("Successfully stopped data generation for database: {}", stopGenerateDataRequestBodyDTO.getDatabaseName());
            return "success";
        } catch (Exception e) {
            log.error("Failed to stop data generation: {}", e.getMessage(), e);
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("tasks")
    public List<InsertDataServer.TaskInfo> getRunningTasks() {
        log.debug("Retrieving list of running tasks");
        List<InsertDataServer.TaskInfo> tasks = insertDataServer.getRunningTasks();
        log.info("Found {} running tasks", tasks.size());
        return tasks;
    }
}
