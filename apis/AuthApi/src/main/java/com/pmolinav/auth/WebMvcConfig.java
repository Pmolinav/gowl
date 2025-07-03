package com.pmolinav.auth;

import com.pmolinav.auth.auth.interceptors.UserAccessInterceptor;
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
        UserAccessInterceptor interceptor = applicationContext.getBean(UserAccessInterceptor.class);

        registry.addInterceptor(interceptor)
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users", "/users/username/**"); // exclude POST /users and GET /users/username/{username}
    }

}