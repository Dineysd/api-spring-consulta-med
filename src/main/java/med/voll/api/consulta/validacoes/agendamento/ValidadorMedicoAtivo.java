package med.voll.api.consulta.validacoes.agendamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.medico.MedicoRepository;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsulta {
	
	@Autowired
    private MedicoRepository repository;

	public ValidadorMedicoAtivo(MedicoRepository repository) {
		super();
		this.repository = repository;
	}


	@Override
	public void validar(DadosAgendamentoConsulta dados) {
		// TODO Auto-generated method stub
		if(dados.idMedico() == null) return;
		
		var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());
		if(!medicoEstaAtivo) {
			throw new ValidacaoException("Consulta não pode ser agendada com o medico removido");
		}
		
	}

}
