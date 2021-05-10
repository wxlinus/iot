/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 
 */

package io.iot.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${project.upload}")
	private String downloadPath;
	@Value("${project.floorPic}")
	private String floorPic;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
        registry.addResourceHandler("/download/**").addResourceLocations(downloadPath);
        registry.addResourceHandler("/floorPic/**").addResourceLocations(floorPic);
        registry.addResourceHandler("/file/**").addResourceLocations("file:/upload/");
        registry.addResourceHandler("/export/**").addResourceLocations("file:C:/");
    }

}