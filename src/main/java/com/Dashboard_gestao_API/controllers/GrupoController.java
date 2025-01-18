package com.Dashboard_gestao_API.controllers;

import com.Dashboard_gestao_API.dtos.GrupoDTO;
import com.Dashboard_gestao_API.models.Grupo;
import com.Dashboard_gestao_API.repositories.GrupoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping(path = "/grupos")
public class GrupoController {
    private GrupoRepository grupoRepository;

    public GrupoController(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @Operation(
            summary = "Buscar grupos",
            description = "Busca todos os grupos cadastrados. Retorna uma lista com os grupos ou uma mensagem de erro caso não existam grupos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de grupos encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))),
                    @ApiResponse(responseCode = "404", description = "Nenhum grupo encontrado")
            }
    )
    @GetMapping
    public ResponseEntity<List<GrupoDTO>> getAll(){
        List<GrupoDTO> Grupos = new ArrayList<>();

        for (Grupo grupo :  this.grupoRepository.findAll()){
            GrupoDTO grupoDTO = new GrupoDTO(
                    grupo.getId(),
                    grupo.getNome()
            );
            Grupos.add(grupoDTO);
        }
        return ResponseEntity.ok(Grupos);
    }
    @Operation(
            summary = "Buscar grupo",
            description = "Busca um grupo pelo ID. Retorna os detalhes do grupo correspondente ou uma mensagem de erro caso não seja encontrado.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do grupo a ser buscado",
                    required = true,
                    in = ParameterIn.PATH
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Grupo encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Grupo.class))),
                    @ApiResponse(responseCode = "404", description = "Grupo não encontrado")
            }
    )
    @GetMapping(path = "/{id}")
    public ResponseEntity<GrupoDTO> getById(@PathVariable(name="id") Long id) {
        Grupo grupoDB = this.grupoRepository.findById(id).orElse(null);

        if (grupoDB != null) {
            return ResponseEntity.ok(new GrupoDTO(grupoDB.getId(), grupoDB.getNome()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @Operation(
            summary = "Criar um novo grupo",
            description = "Cria um novo grupo com os dados fornecidos. " +
                    "O campo `id` é gerado automaticamente pelo sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nome",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GrupoDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Grupo criado com sucesso",
                            content = @Content(schema = @Schema(implementation = Grupo.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Nome inválido"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody GrupoDTO grupoDTO){
        Grupo novoGrupo = new Grupo();
        novoGrupo.setNome(grupoDTO.nome());

        this.grupoRepository.save(novoGrupo);
        return ResponseEntity.ok(novoGrupo);
    }
    @Operation(
            summary = "Alterar grupo",
            description = "Altera as informações de um grupo, como nome e descrição, pelo ID do grupo.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do grupo a ser alterado",
                    required = true,
                    in = ParameterIn.PATH
            ),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do grupo a serem atualizados (nome, descrição)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GrupoDTO.class)
                    )
            )
    )
    @PutMapping(path = "/{id}")
    public ResponseEntity<String> update(@PathVariable(name="id") Long id, @RequestBody Grupo grupo) {
        Grupo grupoDB = this.grupoRepository.findById(id).orElse(null);

        if (grupoDB != null) {
            grupoDB.setNome(grupo.getNome());
            this.grupoRepository.save(grupoDB);
            return ResponseEntity.ok("Grupo atualizado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo não encontrado");
    }
    @Operation(
            summary = "Deletar grupo",
            description = "Deleta um grupo pelo ID. Se o grupo for encontrado, ele será removido do banco de dados.",
            parameters = @Parameter(
                    name = "id",
                    description = "ID do grupo a ser deletado",
                    required = true,
                    in = ParameterIn.PATH
            )
    )
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> delete(@PathVariable(name="id") Long idGrupo){
        Grupo grupoRemover = this.grupoRepository.findById(idGrupo).orElse(null);
        if (grupoRemover != null){
            this.grupoRepository.delete(grupoRemover);
            return ResponseEntity.status(HttpStatus.OK).body("Grupo removido com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Grupo não encontrado");
    }
}
