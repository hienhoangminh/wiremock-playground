package com.rocktester.automation.mdaqchallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

@Configuration
public class SimpleUrlHandlerMappingConfig {

    @Bean
    public SimpleUrlHandlerMapping wireMockControllerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        Properties urlProperties = new Properties();
        urlProperties.put("/*", "wireMockController");
        mapping.setMappings(urlProperties);
        mapping.setOrder(Integer.MAX_VALUE - 1);
        return mapping;
    }

    @Bean
    public SimpleUrlHandlerMapping wireMockAdminControllerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        Properties urlProperties = new Properties();
        urlProperties.put("/__admin/*", "wireMockAdminController");
        mapping.setMappings(urlProperties);
        mapping.setOrder(Integer.MAX_VALUE - 2);
        return mapping;
    }
}
