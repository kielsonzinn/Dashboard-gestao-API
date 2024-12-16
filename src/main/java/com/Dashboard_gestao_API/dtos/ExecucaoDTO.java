package com.Dashboard_gestao_API.dtos;

import java.time.LocalDateTime;

public record ExecucaoDTO(Long id, ProjetoDTO projeto, LocalDateTime dataHoraExecucao,
                          LocalDateTime dataHoraSolicitacao, Integer quantidadeIssue, Integer status) {
}
