/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 
 */

package io.iot.common.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Swagger配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig{
	
	@Value("${project.swagger.show}")
	private boolean swaggerShow;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        	.enable(swaggerShow)
            .apiInfo(apiInfo())
            .select()
            //加了ApiOperation注解的类，生成接口文档
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            //包下的类，生成接口文档
            //.apis(RequestHandlerSelectors.basePackage("io.renren.modules.job.controller"))
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("瑞金管理系统接口")
            .description("ruijin-admin文档")
            .termsOfServiceUrl("https://www.scidream.com")
            .version("4.0.0")
            .build();
    }
    
    private List<ApiKey> security() {
        return newArrayList(
            new ApiKey("JSESSIONID", "JSESSIONID", "header")
        );
    }

}