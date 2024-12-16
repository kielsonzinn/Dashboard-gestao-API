package com.Dashboard_gestao_API.controllers;

import com.Dashboard_gestao_API.dtos.GrupoDTO;
import com.Dashboard_gestao_API.models.Grupo;
import com.Dashboard_gestao_API.repositories.GrupoRepository;
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

    @GetMapping(path = "/{id}")
    public ResponseEntity<GrupoDTO> getById(@PathVariable(name="id") Long id) {
        Grupo grupoDB = this.grupoRepository.findById(id).orElse(null);

        if (grupoDB != null) {
            return ResponseEntity.ok(new GrupoDTO(grupoDB.getId(), grupoDB.getNome()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody GrupoDTO grupoDTO){
        Grupo novoGrupo = new Grupo();
        novoGrupo.setNome(grupoDTO.nome());

        this.grupoRepository.save(novoGrupo);
        return ResponseEntity.ok(novoGrupo);
    }

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
