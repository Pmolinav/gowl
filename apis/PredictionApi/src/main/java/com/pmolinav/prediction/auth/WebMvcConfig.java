package com.pmolinav.prediction.auth;

import com.pmolinav.prediction.auth.interceptors.PlayerBetAccessInterceptor;
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
        PlayerBetAccessInterceptor interceptor = applicationContext.getBean(PlayerBetAccessInterceptor.class);

        registry.addInterceptor(interceptor)
                .addPathPatterns("/player-bets/**");

    }

}