package com.Dashboard_gestao_API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExecucaoInsertDTO(
        @Schema(description = "ID da execução, gerado automaticamente pelo sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,
        ProjetoDTO projeto) {

}
