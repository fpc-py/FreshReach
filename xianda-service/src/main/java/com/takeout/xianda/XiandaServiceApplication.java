package com.takeout.xianda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.takeout.xianda.mapper")

@SpringBootApplication
public class XiandaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiandaServiceApplication.class, args);
    }

}
