package com.rocktester.automation.mdaqchallenge.config;

import com.github.tomakehurst.wiremock.servlet.WireMockHandlerDispatchingServlet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.ServletWrappingController;

import java.util.Properties;

@Configuration
public class ServletWrappingControllerConfig {

    @Bean
    public ServletWrappingController wireMockController() {
        ServletWrappingController controller = new ServletWrappingController();
        controller.setServletClass(WireMockHandlerDispatchingServlet.class);
        controller.setBeanName("wireMockController");
        Properties properties = new Properties();
        properties.setProperty("RequestHandlerClass", "com.github.tomakehurst.wiremock.http.StubRequestHandler");
        controller.setInitParameters(properties);
        return controller;
    }

    @Bean
    public ServletWrappingController wireMockAdminController() {
        ServletWrappingController controller = new ServletWrappingController();
        controller.setServletClass(WireMockHandlerDispatchingServlet.class);
        controller.setBeanName("wireMockAdminController");
        Properties properties = new Properties();
        properties.setProperty("RequestHandlerClass", "com.github.tomakehurst.wiremock.http.AdminRequestHandler");
        controller.setInitParameters(properties);
        return controller;
    }

}
