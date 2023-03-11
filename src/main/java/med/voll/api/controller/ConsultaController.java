package med.voll.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.consulta.AgendaDeConsultas;
import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.consulta.DadosDetalhamentoConsulta;
import med.voll.api.paciente.DadosListagemPaciente;

@RestController
@RequestMapping("consultas")
public class ConsultaController {
	
 @Autowired
 private AgendaDeConsultas agenda;

 @PostMapping
 @Transactional
 public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados, UriComponentsBuilder uriBuilder){
	 var consulta = agenda.agendar(dados);
	 
	 URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(consulta.id()).toUri();
	 
     return ResponseEntity.created(uri).body(consulta);
 }
 
 @GetMapping
 public ResponseEntity<Page<DadosDetalhamentoConsulta>> listar(@PageableDefault(size=10, sort= {"data"}, direction = Direction.DESC)Pageable paginacao) {
     var page = agenda.buscarTodos(paginacao);
     return ResponseEntity.ok(page);
 }

}
