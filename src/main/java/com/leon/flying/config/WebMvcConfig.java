package com.leon.flying.config;

import com.leon.flying.component.LoginHandlerInterceptor;
import com.leon.flying.component.MyLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter  {

    // 如果继承了WebMvcConfigurationSupport,会覆盖访问静态资源的配置，无法访问静态资源，必须继承WebMvcConfigurerAdapter
    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
        //是否是后缀模式匹配,如果是true,请求download.* 可以映射到download
        pathMatchConfigurer.setUseSuffixPatternMatch(true);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/mzadmin").setViewName("login");
    }
//
    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry){
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/index.html").setViewName("login");
                registry.addViewController("/main.html").setViewName("dashboard");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/index.html","/","/user/login");
            }
        };
    }
//
    @Bean
    public LocaleResolver localeResolver(){
        return new MyLocaleResolver();
    }
}
