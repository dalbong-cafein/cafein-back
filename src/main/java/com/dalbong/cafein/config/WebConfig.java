package com.dalbong.cafein.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://cafein-env.eba-ipjcypwd.ap-northeast-2.elasticbeanstalk.com")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowCredentials(true);

    }
}
