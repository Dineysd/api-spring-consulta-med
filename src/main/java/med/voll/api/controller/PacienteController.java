package med.voll.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import med.voll.api.model.Paciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosListagemPaciente;
import med.voll.api.paciente.PacienteRepository;

@RestController
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteRepository repository;
	
	@PostMapping
	public void cadastrar(@RequestBody DadosCadastroPaciente dados) {
		
		repository.save(new Paciente(dados));
		
		System.out.println(dados);
		
	}
	
	@GetMapping
    public Page<DadosListagemPaciente> listar(@PageableDefault(size=10, sort= {"nome"})Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemPaciente::new);
    }

}
