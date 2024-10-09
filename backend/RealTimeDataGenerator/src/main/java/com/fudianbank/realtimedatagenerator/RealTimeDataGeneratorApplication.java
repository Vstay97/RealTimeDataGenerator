package com.fudianbank.realtimedatagenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealTimeDataGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealTimeDataGeneratorApplication.class, args);
        // InsertionController controller = new InsertionController();
        // // 示例：启动MySQL数据库插入
        // controller.startInsertion("mysql", "Test_A", 1, "jdbc:mysql://localhost:3306/test_DB", "root", "songchao");
        // // 示例：启动Oracle数据库插入
        // // controller.startInsertion("oracle", "Test_B", 1, "jdbc:oracle:thin:@localhost:1521:XE", "system", "password");
        // try {
        //     Thread.sleep(15000);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }
        // // 停止MySQL数据库插入
        // controller.stopInsertion("mysql", "Test_A");
    }

}
