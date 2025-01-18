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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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

    public ExecucaoController(ExecucaoRepository execucaoRepository, ProjetoRepository projetoRepository, KafkaProducerService kafkaService) {
        this.execucaoRepository = execucaoRepository;
        this.projetoRepository = projetoRepository;
        this.kafkaService = kafkaService;
    }

    @Operation(
            summary = "Buscar execuções",
            description = "Busca todas as execuções cadastradas. Retorna uma lista de execuções ou uma mensagem de erro caso não existam execuções.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de execuções encontrada",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Execucao.class)))),
                    @ApiResponse(responseCode = "404", description = "Nenhuma execução encontrada")
            }
    )
    @GetMapping
    public ResponseEntity<List<ExecucaoDTO>> getAll() {
        List<Execucao> execucoes = execucaoRepository.findAll();
        List<ExecucaoDTO> dtos = new ArrayList<>();

        for (Execucao execucao : execucoes) {
            dtos.add(new ExecucaoDTO(execucao.getId(), execucao.getDescricao(), new ProjetoDTO(execucao.getProjeto().getId(), execucao.getProjeto().getNome(), execucao.getProjeto().getUrl(), execucao.getProjeto().getBranch(), execucao.getProjeto().getIdGrupo()), execucao.getDataHoraIniciada(), execucao.getDataHoraSolicitacao(), execucao.getQuantidadeIssue(), execucao.getStatus()));
        }

        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Buscar execução",
            description = "Busca execução pelo ID. Retorna a execução correspondente ou uma mensagem de erro caso não seja encontrada.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID da execução a ser buscada",
                    required = true,
                    in = ParameterIn.PATH
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Execução encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Execucao.class))),
                    @ApiResponse(responseCode = "404", description = "Execução não encontrada")
            }
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity<ExecucaoDTO> getById(@PathVariable(name = "id") Long id) {
        Execucao execucao = this.execucaoRepository.findById(id).orElse(null);

        if (execucao != null) {
            return ResponseEntity.ok(new ExecucaoDTO(execucao.getId(), execucao.getDescricao(), new ProjetoDTO(execucao.getProjeto().getId(), execucao.getProjeto().getNome(), execucao.getProjeto().getUrl(), execucao.getProjeto().getBranch(), execucao.getProjeto().getIdGrupo()), execucao.getDataHoraIniciada(), execucao.getDataHoraSolicitacao(), execucao.getQuantidadeIssue(), execucao.getStatus()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @Operation(
            summary = "Deletar execução",
            description = "Deleta ou para uma execução pelo ID. Retorna uma confirmação de sucesso ou erro caso a execução não seja encontrada.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID da execução a ser deletada ou parada",
                    required = true,
                    in = ParameterIn.PATH
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Execução deletada/parada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Execução não encontrada")
            }
    )
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

    @Operation(
            summary = "Alterar execução",
            description = "Altera ou reprocessa uma execução pelo ID. Retorna a execução alterada ou uma mensagem de erro caso não seja encontrada.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID da execução a ser alterada ou reprocessada",
                    required = true,
                    in = ParameterIn.PATH
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Execução alterada/reprocessada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Execucao.class))),
                    @ApiResponse(responseCode = "404", description = "Execução não encontrada")
            }
    )
    @PutMapping(path = "/{id}/reprocessar")
    public ResponseEntity<String> reprocessar(@PathVariable(name = "id") Long id) {
        Execucao execucao = this.execucaoRepository.findById(id).orElse(null);

        if (execucao != null) {
            execucao.setStatus(0);
            this.execucaoRepository.save(execucao);

            List<Execucao> execucoes = new ArrayList<>();
            execucoes.add(execucao);
            kafkaService.sendMessage("execution", execucoes);

            return ResponseEntity.ok("Execução adicionado para processamento");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Execução não encontrada");
    }

    @Operation(
            summary = "Cadastrar uma execução",
            description = "Cadastra uma nova execução associada a um projeto. Retorna a execução cadastrada ou uma mensagem de erro caso o projeto não seja encontrado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da execução a serem cadastrados (Descrição, Status, Quantidade de Issues, etc.)",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExecucaoInsertDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Execução cadastrada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Execucao.class))),
                    @ApiResponse(responseCode = "400", description = "Projeto não encontrado",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody ExecucaoInsertDTO execucaoInsertDTO) {

        Execucao novaExecucao = new Execucao();
        novaExecucao.setProjeto(projetoRepository.findById(execucaoInsertDTO.projeto().id()).orElse(null));
        novaExecucao.setDataHoraIniciada(LocalDateTime.now());
        novaExecucao.setStatus(0);

        if (novaExecucao.getProjeto() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Projeto não encontrado");
        }

        this.execucaoRepository.save(novaExecucao);

        List<Execucao> execucoes = new ArrayList<>();
        execucoes.add(novaExecucao);
        kafkaService.sendMessage("execution", execucoes);

        return ResponseEntity.ok(novaExecucao);

    }

}
