package com.Dashboard_gestao_API.dtos;

public record IssueDTO(Long id, String descricao, String arquivo, Integer linhaInicial, Integer linhaFinal) {
}
