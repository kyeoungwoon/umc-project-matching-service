package kr.kyeoungwoon.upms.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    String accessToken = "UPMS Access Token";

    return new OpenAPI()
        .info(new Info()
            .title("UPMS API")
            .version("1.0.0")
            .description("UMC Project Matching Service, UPMS"))
        .servers(List.of(
            new Server()
                .url("http://localhost:8080")
                .description("Local")
            , new Server().url("https://api.upms.kyeoungwoon.kr").description("Production"),
            new Server().url("https://api.dev-upms.kyeoungwoon.kr").description("Production")
        ))
        .addSecurityItem(new SecurityRequirement()
            .addList(accessToken)
        )
        .components(new Components()
            .addSecuritySchemes(accessToken,
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("UPMS JWT")
            )
        );
  }

  @Bean
  public ModelResolver modelResolver(ObjectMapper objectMapper) {
    // Spring이 관리하는 ObjectMapper를 Swagger에 전달
    return new ModelResolver(objectMapper);
  }
}
