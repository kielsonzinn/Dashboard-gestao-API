package com.Dashboard_gestao_API.dtos;

import java.time.LocalDateTime;

public record ExecucaoDTO(Long id, String descricao, ProjetoDTO projeto, LocalDateTime dataHoraIniciada,
                          LocalDateTime dataHoraSolicitacao, Integer quantidadeIssue, Integer status) {
}
