package com.panch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.panch.mapper")
@EnableScheduling  // 开启定时任务
@EnableSwagger2 //启动swagger
public class panchBlogApplication {
        public static void main(String[] args) {
            SpringApplication.run(panchBlogApplication.class, args);
        }
}
