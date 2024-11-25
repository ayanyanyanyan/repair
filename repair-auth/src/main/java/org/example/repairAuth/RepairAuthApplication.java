package org.example.repairAuth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ayan
 */
@SpringBootApplication
@MapperScan("org.example.repairAuth.mapper")
public class RepairAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(RepairAuthApplication.class, args);
    }

}
