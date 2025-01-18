package com.Dashboard_gestao_API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GrupoDTO(
        @Schema(description = "ID do grupo, gerado automaticamente pelo sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Nome do grupo", example = "Grupo de Desenvolvimento", required = true)
        @NotBlank(message = "Nome do grupo é obrigatório")
        @NotNull(message = "Nome do grupo é obrigatório")
        String nome
) {

}
