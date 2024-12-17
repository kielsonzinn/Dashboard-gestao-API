package com.Dashboard_gestao_API.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ISSUES")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issues_seq")
    @SequenceGenerator(name = "issues_seq", sequenceName = "issues_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    private String descricao;

    private String arquivo;

    private Integer linhaInicial;

    private Integer linhaFinal;

    private String tipoIssue;

}
