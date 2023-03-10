package med.voll.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("consultas")
public class ConsultaController {
	
 @Autowired
 private AgendaDeConsultas agenda;

 @PostMapping
 @Transactional
 public ResponseEntity<?> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados, UriComponentsBuilder uriBuilder){
	 var consulta = agenda.agendar(dados);
	 
	 URI uri = uriBuilder.path("/medicos/{id}").buildAndExpand(consulta.getId()).toUri();
	 
     return ResponseEntity.created(uri).body(new DadosDetalhamentoConsulta(consulta.getId(), consulta.getMedico().getId(), consulta.getPaciente().getId(), consulta.getData()));
 }

}
