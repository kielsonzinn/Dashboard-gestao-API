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
@Table(name = "PROJETOS")
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projeto_seq")
    @SequenceGenerator(name = "projeto_seq", sequenceName = "projeto_seq", allocationSize = 1)
    private Long id;
    private String nome;
    private String url;
    private String branch;
}
