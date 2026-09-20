package com.unisantos.clinica_api.config;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${app.cors.allowed-origins:http://localhost:3000}")
  private String[] allowedOrigins;

  @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
  private String[] allowedMethods;

  @Value("${app.cors.allowed-headers:Authorization,Content-Type}")
  private String[] allowedHeaders;

  @Value("${app.cors.allow-credentials:true}")
  private boolean allowCredentials;

  @Value("${app.cors.max-age:3600}")
  private long maxAge;

  private final Environment environment;

  public CorsConfig(Environment environment) {
    this.environment = environment;
  }

  @PostConstruct
  void rejectLocalhostFallbackInProduction() {
    if (!Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
      return;
    }
    boolean hasLocalhost =
        Arrays.stream(allowedOrigins)
            .anyMatch(origin -> origin == null || origin.contains("localhost"));
    if (hasLocalhost) {
      throw new IllegalStateException(
          "app.cors.allowed-origins must be set explicitly in prod and must not use localhost");
    }
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/api/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods(allowedMethods)
        .allowedHeaders(allowedHeaders)
        .allowCredentials(allowCredentials)
        .maxAge(maxAge);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(allowedOrigins));
    configuration.setAllowedMethods(List.of(allowedMethods));
    configuration.setAllowedHeaders(List.of(allowedHeaders));
    configuration.setAllowCredentials(allowCredentials);
    configuration.setMaxAge(maxAge);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
  }
}
