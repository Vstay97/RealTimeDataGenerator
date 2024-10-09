package com.fudianbank.realtimedatagenerator.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "实时数据流生成",
                version = "1.0",
                description = "用于各类数据库的实时数据流生成",
                contact = @Contact(name = "宋超", email = "vstay@qq.com")
        ),
        externalDocs = @ExternalDocumentation(url = "http://www.fudian-bank.com", description = "富滇银行股份有限公司")
)
public class Swagger3Config {

}
