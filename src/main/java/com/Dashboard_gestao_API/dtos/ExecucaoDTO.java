package com.Dashboard_gestao_API.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "Dados de execução")
public record ExecucaoDTO(
        @Schema(description = "ID da execução, gerado automaticamente pelo sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Descrição da execução", example = "Execução do projeto XYZ")
        @NotBlank(message = "Descrição da execução é obrigatória")
        @NotNull(message = "Descrição da execução é obrigatória")
        String descricao,

        @Schema(description = "Detalhes do projeto associado", implementation = ProjetoDTO.class)
        @NotBlank(message = "Detalhes do projeto associado é obrigatório")
        @NotNull(message = "Detalhes do projeto associado é obrigatório")
        ProjetoDTO projeto,

        @Schema(description = "Data e hora em que a execução foi iniciada", example = "2025-01-18T12:00:00")
        @NotNull(message = "Data e hora de início são obrigatórios")
        LocalDateTime dataHoraIniciada,

        @Schema(description = "Data e hora da solicitação da execução", example = "2025-01-17T15:30:00")
        @NotNull(message = "Data e hora de início são obrigatórios")
        LocalDateTime dataHoraSolicitacao,

        @Schema(description = "Quantidade de issues associadas à execução", example = "10")
        @Min(value = 0, message = "A quantidade de issues não pode ser negativa")
        @NotNull(message = "A quantidade de issues não pode ser nula")
        Integer quantidadeIssue,

        @Schema(description = "Status da execução (0 = em andamento, 1 = concluída)", example = "0")
        @NotNull(message = "Status é obrigatório")
        Integer status
) {
}
