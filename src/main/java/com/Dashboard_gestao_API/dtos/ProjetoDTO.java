package com.Dashboard_gestao_API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjetoDTO(
        @Schema(description = "ID do projeto, gerado automaticamente pelo sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id, // O campo id será apenas mostrado na resposta, mas não será enviado na requisição

        @Schema(description = "Nome do projeto", example = "Meu Projeto", required = true)
        @NotBlank(message = "Nome do projeto é obrigatório")
        @NotNull(message = "Nome do projeto é obrigatório")
        String nome,

        @Schema(description = "URL do repositório Git", example = "https://github.com/usuario/projeto", required = true)
        @NotBlank(message = "URL do projeto é obrigatória")
        @NotNull(message = "URL do projeto é obrigatória")
        String url,

        @Schema(description = "Branch do repositório Git", example = "main", required = true)
        @NotBlank(message = "Branch do projeto é obrigatória")
        @NotNull(message = "Branch do projeto é obrigatória")
        String branch,

        @NotNull(message = "Grupo ao qual o projeto pertence obrigatório")
        @Schema(description = "ID do Grupo ao qual o projeto pertence", example = "100", required = true)
        Long idGrupo
) {}
