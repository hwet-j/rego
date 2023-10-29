package com.clipclap.rego.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    /* 웹 주소로 입력시 자동으로  서버의 C드라이브에서 가져와 사진을 출력합니다 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/home/opc/asset/"); // 변경된 로컬 경로
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
