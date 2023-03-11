package med.voll.api.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.consulta.ConsultaRepository;
import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.infra.exception.ValidacaoException;

@Component
public class ValidadorMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsulta{

	@Autowired
	private ConsultaRepository repository;
	
	@Override
	public void validar(DadosAgendamentoConsulta dados) {
		// TODO Auto-generated method stub
		var medicoComOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(dados.idMedico(), dados.data());
		if(medicoComOutraConsultaNoMesmoHorario) {
			throw new ValidacaoException("Medico ja possui outra consulta agendada nesse mesmo horario");
		}
		
	}

}
