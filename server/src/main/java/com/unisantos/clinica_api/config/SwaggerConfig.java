package com.unisantos.clinica_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI clinicaOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
        .info(
            new Info()
                .title("PsiUnisantos API")
                .version("1.0")
                .description(
                    "API for the PsiUnisantos Clinic Management System.\n\n"
                        + "Endpoints marked with a lock icon require a Bearer access token. "
                        + "To try them here:\n\n"
                        + "1. Run `POST /api/v1/auth/login` with a valid account\n"
                        + "2. Copy `accessToken` from the response\n"
                        + "3. Click **Authorize** (top right) and paste the token\n\n"
                        + "Every request then sends `Authorization: Bearer <token>` "
                        + "automatically.")
                .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
        .servers(
            List.of(new Server().url("http://localhost:8080").description("Development Server")))
        // Tag order in Swagger UI: Authentication first (get a token), Users
        // second, everything else alphabetical (with tags-sorter=none).
        .tags(
            List.of(
                new Tag().name("Authentication"),
                new Tag().name("Users"),
                new Tag().name("Lookups"),
                new Tag().name("Patients"),
                new Tag().name("Professionals")))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(
            new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(
                    securitySchemeName,
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")));
  }

  /**
   * Orders tags in Swagger UI: Authentication first (where you grab a token), Users second,
   * everything else alphabetical. Runs after scanning, so it also dedupes tags declared both here
   * and via {@code @Tag} on controllers (springdoc merges without deduping).
   */
  @Bean
  public OpenApiCustomizer tagOrderCustomizer() {
    List<String> pinned = List.of("Authentication", "Users");
    return openApi -> {
      if (openApi.getTags() == null) {
        return;
      }
      List<Tag> tags =
          openApi.getTags().stream()
              .collect(Collectors.toMap(Tag::getName, t -> t, (a, b) -> a, LinkedHashMap::new))
              .values()
              .stream()
              .sorted(
                  Comparator.comparingInt(
                          (Tag t) -> {
                            int i = pinned.indexOf(t.getName());
                            return i >= 0 ? i : pinned.size();
                          })
                      .thenComparing(t -> pinned.contains(t.getName()) ? "" : t.getName()))
              .toList();
      openApi.setTags(tags);
    };
  }
}
