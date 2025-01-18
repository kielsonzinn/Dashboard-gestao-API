package com.Dashboard_gestao_API.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Dashboard Gestão",
                version = "1.0",
                description = "API para gerenciamento de projetos, Grupos e Execuções"
        )
)
public class OpenAPIConfig {
    // Configuração adicional, se necessário
    //@Bean
    //public OpenAPI customOpenAPI() {
    //    return new OpenAPI()
    //            .info(apiInfo())  // Informações sobre a API
    //            .servers(apiServers())  // Definição dos servidores da API
    //            .addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // Requisito de segurança
    //            .schemaRequirement("bearerAuth", securityScheme()); // Esquema de segurança
    //}
}
