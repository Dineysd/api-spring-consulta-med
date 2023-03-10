package med.voll.api.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import med.voll.api.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.model.Consulta;
import med.voll.api.model.Medico;
import med.voll.api.paciente.PacienteRepository;

@Service
public class AgendaDeConsultas {

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private MedicoRepository medicoRepository;

	@Autowired
	private PacienteRepository pacienteRepository;
	
	@Autowired
	private List<ValidadorAgendamentoDeConsulta> validadores;

	public Consulta agendar(DadosAgendamentoConsulta dados) {

		if (!pacienteRepository.existsById(dados.idPaciente())) {
			throw new ValidacaoException("Id do paciente informado não existe!");

		}
		if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
			throw new ValidacaoException("Id do médico informado não existe!");
		}
		
		validadores.forEach(v -> v.validar(dados));

		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var medico = escolherMedico(dados);

		var consulta = new Consulta(null, medico, paciente, dados.data());
		consultaRepository.save(consulta);
		return consulta;
	}

	private Medico escolherMedico(DadosAgendamentoConsulta dados) {
		if (dados.idMedico() != null) {
			return medicoRepository.getReferenceById(dados.idMedico());
		}
		if (dados.especialidade() == null) {
			throw new ValidacaoException("Especialidade é obrigatória quando médico não for escolhido!");
		}
		return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
	}
}
