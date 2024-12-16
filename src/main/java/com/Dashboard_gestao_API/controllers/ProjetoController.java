package com.Dashboard_gestao_API.controllers;

import com.Dashboard_gestao_API.dtos.IssueDTO;
import com.Dashboard_gestao_API.dtos.ProjetoDTO;
import com.Dashboard_gestao_API.models.Grupo;
import com.Dashboard_gestao_API.models.Issue;
import com.Dashboard_gestao_API.models.Projeto;
import com.Dashboard_gestao_API.repositories.IssueRepository;
import com.Dashboard_gestao_API.repositories.ProjetoRepository;
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

    @GetMapping
    public ResponseEntity<List<ProjetoDTO>> getAll(){
        List<ProjetoDTO> Projetos = new ArrayList<>();

        for (Projeto projeto :  this.projetoRepository.findAll()){
            ProjetoDTO projetoDTO = new ProjetoDTO(
                    projeto.getId(),
                    projeto.getNome(),
                    projeto.getUrl(),
                    projeto.getBranch()
            );
            Projetos.add(projetoDTO);
        }
        return ResponseEntity.ok(Projetos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProjetoDTO> getById(@PathVariable(name = "id") Long id) {
        Projeto projetoDB = this.projetoRepository.findById(id).orElse(null);

        if (projetoDB != null) {
            return ResponseEntity.ok(new ProjetoDTO(projetoDB.getId(), projetoDB.getNome(), projetoDB.getUrl(), projetoDB.getBranch()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(path = "/{id}/issues")
    public ResponseEntity<List<IssueDTO>> getIssuesById(@PathVariable(name = "id") Long id) {
        Projeto projetoDB = this.projetoRepository.findById(id).orElse(null);

        if (projetoDB != null) {
            List<Issue> issues = this.issueRepository.findAllByProjetoEquals(projetoDB);
            List<IssueDTO> dtos = new ArrayList<>();

            for (Issue issue : issues) {
                dtos.add(new IssueDTO(issue.getId(), issue.getDescricao(), issue.getArquivo(), issue.getLinhaInicial(), issue.getLinhaFinal()));
            }

            return ResponseEntity.ok(dtos);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProjetoDTO projetoDTO){
        Projeto novoProjeto = new Projeto();
        novoProjeto.setNome(projetoDTO.nome());
        novoProjeto.setBranch(projetoDTO.branch());
        novoProjeto.setUrl(projetoDTO.url());

        //TODO AQUI TAMBEM DEVE SALVAR O ID GRUPO RELACIONADO

        this.projetoRepository.save(novoProjeto);
        return ResponseEntity.ok(novoProjeto);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> update(@PathVariable(name="id") Long id, @RequestBody Projeto projeto) {
        Projeto projetoDB = this.projetoRepository.findById(id).orElse(null);

        if (projetoDB != null) {
            projetoDB.setNome(projeto.getNome());
            projetoDB.setBranch(projeto.getBranch());
            projetoDB.setUrl(projeto.getUrl());
            this.projetoRepository.save(projetoDB);
            return ResponseEntity.ok("Projeto atualizado com sucesso!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projeto não encontrado");
    }

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
