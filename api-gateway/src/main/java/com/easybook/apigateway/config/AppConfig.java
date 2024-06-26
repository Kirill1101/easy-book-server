package com.easybook.apigateway.config;

import java.util.Arrays;
import java.util.List;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class AppConfig {

  @Bean
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            r ->
                r.path("/auth-service/v3/api-docs")
                    .and()
                    .method(HttpMethod.GET)
                    .uri("lb://auth-service"))
        .route(
            r ->
                r.path("/scheduling-service/v3/api-docs")
                    .and()
                    .method(HttpMethod.GET)
                    .uri("lb://scheduling-service"))
        .build();
  }

  @Bean
  public CorsWebFilter corsFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowCredentials(true);
    corsConfiguration.setAllowedOriginPatterns(
        List.of("http://localhost:[*]", "https://localhost:[*]", "null"));

    corsConfiguration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
    corsConfiguration.setAllowedHeaders(
        Arrays.asList("origin", "content-type", "accept", "Authorization", "cookie"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsWebFilter(source);
  }
}
