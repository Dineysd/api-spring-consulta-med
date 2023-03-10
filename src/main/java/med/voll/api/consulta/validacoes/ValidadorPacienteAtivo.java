package med.voll.api.consulta.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.paciente.PacienteRepository;

@Component
public class ValidadorPacienteAtivo implements ValidadorAgendamentoDeConsulta{
	
	@Autowired
    private PacienteRepository repository;

	@Override
	public void validar(DadosAgendamentoConsulta dados) {
		if(dados.idPaciente() == null) return;
		
		var pacienteEstaAtivo = repository.findAtivoById(dados.idPaciente());
		if(!pacienteEstaAtivo) {
			throw new ValidacaoException("Consulta não pode ser agendada com paciente excluido");
		}
		
	}

}
