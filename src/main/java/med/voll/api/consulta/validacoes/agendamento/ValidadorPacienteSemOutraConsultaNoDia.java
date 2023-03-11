package med.voll.api.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.infra.exception.ValidacaoException;

@Component
public class ValidadorPacienteSemOutraConsultaNoDia implements ValidadorAgendamentoDeConsulta{
	
	@Autowired
	private ConsultaRepository repository;

	@Override
	public void validar(DadosAgendamentoConsulta dados) {
		// TODO Auto-generated method stub
		var primeiroHorario = dados.data().withHour(7);
		var ultimoHorario = dados.data().withHour(18);
		var pacientePossuiOutraConsulta = repository.existsByPacienteIdAndDataBetween(dados.idPaciente(), primeiroHorario, ultimoHorario);
		
		if(pacientePossuiOutraConsulta) {
			throw new ValidacaoException("Paciente já possui uma consulta agendada nesse dia!");
		}
		
	}

}
