package com.takeout.xianda.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("FreshReach API文档")
                        .version("1.0")
                        .description("外卖平台接口文档")
                        .contact(new Contact()
                                .name("fpc开发")
                                .email("fpc2024666@qq.com")));
    }
}
