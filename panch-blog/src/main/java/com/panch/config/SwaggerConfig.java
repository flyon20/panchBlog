package com.panch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.panch.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("panch", "http://www.my.com", "xue_1908@163.com");
        return new ApiInfoBuilder()
                .title("博客系统")
                .description("多用户转评阅博客管理系统")
                .contact(contact)   // 联系方式
                .version("0.1.0")  // 版本
                .build();
    }
}