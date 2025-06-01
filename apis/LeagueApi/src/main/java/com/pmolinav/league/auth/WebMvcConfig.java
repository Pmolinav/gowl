package com.pmolinav.league.auth;

import com.pmolinav.league.auth.interceptors.LeagueAccessInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    public WebMvcConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LeagueAccessInterceptor interceptor = applicationContext.getBean(LeagueAccessInterceptor.class);
        registry.addInterceptor(interceptor)
                .addPathPatterns("/leagues/**")
                .excludePathPatterns("/leagues"); // exclude POST /leagues
    }

}