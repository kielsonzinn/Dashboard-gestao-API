package com.Dashboard_gestao_API.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EXECUCOES")
public class Execucao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "execucao_seq")
    @SequenceGenerator(name = "execucao_seq", sequenceName = "execucao_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    private String descricao;

    private LocalDateTime dataHoraSolicitacao;

    private LocalDateTime dataHoraIniciada;

    private LocalDateTime dataHoraFinalizada;

    private LocalDateTime dataHoraExecucao;

    private Integer quantidadeIssue;

    /*
    0 - PENDENTE
    1 - EXECUTADA_COM_SUCESSO
    2 - EXECUTADA_COM_FALHA
    3 - CANCELADO
     */
    private Integer status; //TODO TRANSFORMAR EM UM ENUM


}
