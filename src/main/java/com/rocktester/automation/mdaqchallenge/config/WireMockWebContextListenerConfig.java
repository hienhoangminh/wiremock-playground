package com.rocktester.automation.mdaqchallenge.config;

import com.github.tomakehurst.wiremock.servlet.WireMockWebContextListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WireMockWebContextListenerConfig {

    @Bean
    public WireMockWebContextListener wireMockWebContextListener() {
        return new WireMockWebContextListener();
    }

}
