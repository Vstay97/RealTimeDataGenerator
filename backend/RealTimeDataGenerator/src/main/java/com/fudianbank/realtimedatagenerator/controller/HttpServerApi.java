package com.fudianbank.realtimedatagenerator.controller;

import com.fudianbank.realtimedatagenerator.entity.StartGenerateDataRequestBodyDTO;
import com.fudianbank.realtimedatagenerator.entity.StopGenerateDataRequestBodyDTO;
import com.fudianbank.realtimedatagenerator.server.InsertDataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Charles
 * @Date 2024-09-24 14:27
 * Belongs to Fudian Bank
 */
@RestController
@RequestMapping("/generator")
public class HttpServerApi {
    @Autowired
    private InsertDataServer insertDataServer;
    @GetMapping("/")
    public String HelloWorld(){
        System.out.println("Hello World!!!");
        return "Hello World";
    }
    @PostMapping ("start")
    public String startGeneratorData(@RequestBody StartGenerateDataRequestBodyDTO startGenerateDataRequestBodyDTO){
        // 打印参数
        System.out.println(startGenerateDataRequestBodyDTO);
        insertDataServer.startInsertion(startGenerateDataRequestBodyDTO.getDbType(), startGenerateDataRequestBodyDTO.getDatabaseName(), startGenerateDataRequestBodyDTO.getThreadCount(), startGenerateDataRequestBodyDTO.getJdbcUrl(), startGenerateDataRequestBodyDTO.getUsername(), startGenerateDataRequestBodyDTO.getPassword());

        // insertionController.startInsertion("mysql", "Test_A", 1, "jdbc:mysql://localhost:3306/test_DB", "root", "songchao");
        // 示例：启动Oracle数据库插入
        // controller.startInsertion("oracle", "Test_B", 1, "jdbc:oracle:thin:@localhost:1521:XE", "system", "password");
        // try {
        //     Thread.sleep(5000);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        return "success";
    }
    @PostMapping ("stop")
    public String stopGeneratorData(@RequestBody StopGenerateDataRequestBodyDTO stopGenerateDataRequestBodyDTO){
        // 打印参数
        System.out.println(stopGenerateDataRequestBodyDTO);
        // 停止MySQL数据库插入
        insertDataServer.stopInsertion(stopGenerateDataRequestBodyDTO.getDbType(), stopGenerateDataRequestBodyDTO.getDatabaseName());
        // insertionController.stopInsertion("mysql", "Test_A");
        return "success";
    }
    @GetMapping("tasks")
    public List<InsertDataServer.TaskInfo> getRunningTasks() {
        System.out.println("getRunningTasks!");
        System.out.println(insertDataServer.getRunningTasks());
        return insertDataServer.getRunningTasks();
    }
}
