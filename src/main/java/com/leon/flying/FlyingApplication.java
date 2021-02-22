package com.leon.flying;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@SpringBootApplication
@MapperScan({"com.leon.flying.dao",
        "com.leon.flying.spider.mapper",
        "com.leon.flying.mapper"})
@EnableScheduling
public class FlyingApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder builder) {
        return builder.sources(FlyingApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(FlyingApplication.class, args);
    }

    @Bean
    public ViewResolver myViewReolver(){
        return new MyViewResolver();
    }

    public static class MyViewResolver implements ViewResolver{

        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return null;
        }
    }

}
