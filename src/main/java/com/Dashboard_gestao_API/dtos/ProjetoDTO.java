package com.Dashboard_gestao_API.dtos;

public record ProjetoDTO(
        Long id,
        String nome,
        String url,
        String branch,
        Long idGrupo
) {
}
