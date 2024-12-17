package com.Dashboard_gestao_API.controllers;

import com.Dashboard_gestao_API.dtos.ExecucaoDTO;
import com.Dashboard_gestao_API.dtos.ExecucaoInsertDTO;
import com.Dashboard_gestao_API.dtos.GrupoDTO;
import com.Dashboard_gestao_API.dtos.ProjetoDTO;
import com.Dashboard_gestao_API.models.Execucao;
import com.Dashboard_gestao_API.models.Grupo;
import com.Dashboard_gestao_API.repositories.ExecucaoRepository;
import com.Dashboard_gestao_API.repositories.ProjetoRepository;
import com.Dashboard_gestao_API.services.KafkaProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/execucoes")
public class ExecucaoController {

    private final ExecucaoRepository execucaoRepository;

    private final ProjetoRepository projetoRepository;

    private final KafkaProducerService kafkaService;

    public ExecucaoController(ExecucaoRepository execucaoRepository, ProjetoRepository projetoRepository, KafkaTemplate<String, String> kafkaTemplate, KafkaProducerService kafkaService) {
        this.execucaoRepository = execucaoRepository;
        this.projetoRepository = projetoRepository;
        this.kafkaService = kafkaService;
    }

    @GetMapping
    public ResponseEntity<List<ExecucaoDTO>> getAll() {
        List<Execucao> execucoes = execucaoRepository.findAll();
        List<ExecucaoDTO> dtos = new ArrayList<>();

        for (Execucao execucao : execucoes) {
            dtos.add(new ExecucaoDTO(execucao.getId(), new ProjetoDTO(execucao.getProjeto().getId(), execucao.getProjeto().getNome(), execucao.getProjeto().getUrl(), execucao.getProjeto().getBranch()), execucao.getDataHoraExecucao(), execucao.getDataHoraSolicitacao(), execucao.getQuantidadeIssue(), execucao.getStatus()));
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ExecucaoDTO> getById(@PathVariable(name = "id") Long id) {
        Execucao execucao = this.execucaoRepository.findById(id).orElse(null);

        if (execucao != null) {
            return ResponseEntity.ok(new ExecucaoDTO(execucao.getId(), new ProjetoDTO(execucao.getProjeto().getId(), execucao.getProjeto().getNome(), execucao.getProjeto().getUrl(), execucao.getProjeto().getBranch()), execucao.getDataHoraExecucao(), execucao.getDataHoraSolicitacao(), execucao.getQuantidadeIssue(), execucao.getStatus()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping(path = "/{id}/abortar")
    public ResponseEntity<String> abortar(@PathVariable(name = "id") Long id) {
        Execucao execucao = this.execucaoRepository.findById(id).orElse(null);

        if (execucao != null) {
            execucao.setStatus(3);
            this.execucaoRepository.save(execucao);
            return ResponseEntity.ok("Execução abortada");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Execução não encontrada");
    }

    @PutMapping(path = "/{id}/reprocessar")
    public ResponseEntity<String> reprocessar(@PathVariable(name = "id") Long id) {
        Execucao execucao = this.execucaoRepository.findById(id).orElse(null);

        if (execucao != null) {
            execucao.setStatus(0);
            this.execucaoRepository.save(execucao);

            //TODO Enviar um objeto (Json) para o kafka
            kafkaService.sendMessage("execution", getPendingExecutionMessage(execucao.getId(), execucao.getProjeto().getId()) );

            return ResponseEntity.ok("Execução adicionado para processamento");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Execução não encontrada");
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody ExecucaoInsertDTO execucaoInsertDTO) {

        Execucao novaExecucao = new Execucao();
        novaExecucao.setProjeto(projetoRepository.findById(execucaoInsertDTO.projeto().id()).orElse(null));
        novaExecucao.setDataHoraExecucao(LocalDateTime.now());
        novaExecucao.setStatus(0);

        if (novaExecucao.getProjeto() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Projeto não encontrado");
        }

        this.execucaoRepository.save(novaExecucao);

         //TODO Enviar um objeto (Json) para o kafka
        kafkaService.sendMessage("execution", getPendingExecutionMessage(novaExecucao.getId(), novaExecucao.getProjeto().getId()) );

        return ResponseEntity.ok(novaExecucao);

    }

    private String getPendingExecutionMessage(Long idExecution, Long idProject) {
        return String.format(
                "{ 'pending': [{ id_execution: %d, id_project: %d }] }",
                idExecution,
                idProject);
    }

}
