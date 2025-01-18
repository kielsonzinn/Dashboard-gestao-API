package com.Dashboard_gestao_API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GRUPOS")
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupo_seq")
    @SequenceGenerator(name = "grupo_seq", sequenceName = "grupo_seq", allocationSize = 1)
    private Long id;
    private String nome;
}
