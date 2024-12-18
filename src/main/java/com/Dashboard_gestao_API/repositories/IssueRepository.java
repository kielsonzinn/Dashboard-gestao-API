package com.Dashboard_gestao_API.repositories;

import com.Dashboard_gestao_API.models.Issue;
import com.Dashboard_gestao_API.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findAllByProjetoEquals(Projeto projeto);

}
