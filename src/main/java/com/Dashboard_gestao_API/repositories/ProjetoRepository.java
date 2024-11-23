package com.Dashboard_gestao_API.repositories;

import com.Dashboard_gestao_API.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
