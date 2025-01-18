package com.Dashboard_gestao_API.controllers;

import com.Dashboard_gestao_API.dtos.IssueDTO;
import com.Dashboard_gestao_API.dtos.ProjetoDTO;
import com.Dashboard_gestao_API.models.Grupo;
import com.Dashboard_gestao_API.models.Issue;
import com.Dashboard_gestao_API.models.Projeto;
import com.Dashboard_gestao_API.repositories.IssueRepository;
import com.Dashboard_gestao_API.repositories.ProjetoRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/projetos")
public class ProjetoController {

    private ProjetoRepository projetoRepository;

    private final IssueRepository issueRepository;

    public ProjetoController(ProjetoRepository projetoRepository, IssueRepository issueRepository) {
        this.projetoRepository = projetoRepository;
        this.issueRepository = issueRepository;
    }
    @Operation(
            summary = "Buscar projetos",
            description = "Busca todos os projetos cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de projetos encontrada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjetoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida")
            }
    )
    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> getAll(){
        List<ProjetoDTO> Projetos = new ArrayList<>();

        for (Projeto projeto :  this.projetoRepository.findAll()){
            ProjetoDTO projetoDTO = new ProjetoDTO(
                    projeto.getId(),
                    projeto.getNome(),
                    projeto.getUrl(),
                    projeto.getBranch(),
                    projeto.getIdGrupo()
            );
            Projetos.add(projetoDTO);
        }
        return ResponseEntity.ok(Projetos);
    }
    @Operation(
            summary = "Buscar um projeto",
            description = "Busca um projeto pelo ID. Retorna os detalhes do projeto correspondente ou uma mensagem de erro caso não seja encontrado.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do projeto a ser buscado",
                    required = true,
                    in = ParameterIn.PATH
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Projeto encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Projeto.class))),
                    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
            }
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjetoDTO> getById(@PathVariable(name = "id") Long id) {
        Projeto projetoDB = this.projetoRepository.findById(id).orElse(null);

        if (projetoDB != null) {
            return ResponseEntity.ok(new ProjetoDTO(projetoDB.getId(), projetoDB.getNome(), projetoDB.getUrl(), projetoDB.getBranch(), projetoDB.getIdGrupo()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @Operation(
            summary = "Lista os problemas de um projeto",
            description = "Busca os problemas de um projeto pelo ID. Retorna uma lista de problemas ou uma mensagem de erro caso o projeto não seja encontrado.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do projeto para buscar os problemas",
                    required = true,
                    in = ParameterIn.PATH
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de problemas encontrada",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Issue.class)))),
                    @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
            }
    )
    @GetMapping(path = "/{id}/issues")
    public ResponseEntity<List<IssueDTO>> getIssuesById(@PathVariable(name = "id") Long id) {
        Projeto projetoDB = this.projetoRepository.findById(id).orElse(null);

        if (projetoDB != null) {
            List<Issue> issues = this.issueRepository.findAllByProjetoEquals(projetoDB);
            List<IssueDTO> dtos = new ArrayList<>();

            for (Issue issue : issues) {
                dtos.add(new IssueDTO(issue.getId(), issue.getDescricao(), issue.getArquivo(), issue.getLinhaInicial(), issue.getLinhaFinal(),issue.getTipoIssue()));
            }

            return ResponseEntity.ok(dtos);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @Operation(
            summary = "Cadastrar um novo projeto",
            description = "Cadastra um novo projeto com os dados fornecidos. O campo `id` é ignorado no cadastro e gerado automaticamente pelo sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criar um novo projeto.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjetoDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Projeto cadastrado com sucesso.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjetoDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida.")
            }
    )
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody ProjetoDTO projetoDTO){
        Projeto novoProjeto = new Projeto();
        novoProjeto.setNome(projetoDTO.nome());
        novoProjeto.setBranch(projetoDTO.branch());
        novoProjeto.setUrl(projetoDTO.url());
        novoProjeto.setIdGrupo(projetoDTO.idGrupo());

        this.projetoRepository.save(novoProjeto);
        return ResponseEntity.ok(novoProjeto);
    }
    @Operation(
            summary = "Alterar um projeto",
            description = "Alterar um projeto pelo ID. Atualiza o nome, branch e URL do projeto.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do projeto a ser alterado",
                    required = true,
                    in = ParameterIn.PATH
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do projeto a serem atualizados (Nome, Branch, URL)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = Projeto.class)
                    )
            )
    )
    @PutMapping(path = "/{id}")
    public ResponseEntity<String> update(@PathVariable(name = "id") Long id, @RequestBody ProjetoDTO projetoDTO) {

        Projeto projetoDB = this.projetoRepository.findById(id).orElse(null);

        if (projetoDB != null) {

            projetoDB.setNome(projetoDTO.nome());
            projetoDB.setBranch(projetoDTO.branch());
            projetoDB.setUrl(projetoDTO.url());
            projetoDB.setIdGrupo(projetoDTO.idGrupo());

            this.projetoRepository.save(projetoDB);


            return ResponseEntity.ok("Projeto atualizado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado");
    }

    @Operation(
            summary = "Deletar um projeto",
            description = "Deleta um projeto pelo ID. Se o projeto for encontrado, ele será removido do banco de dados.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do projeto a ser deletado",
                    required = true,
                    in = ParameterIn.PATH
            )
    )
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable(name="id") Long idProjeto){
        Projeto projetoRemover = this.projetoRepository.findById(idProjeto).orElse(null);
        if (projetoRemover != null){
            this.projetoRepository.delete(projetoRemover);
            return ResponseEntity.status(HttpStatus.OK).body("Projeto removido com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado");
    }
}
