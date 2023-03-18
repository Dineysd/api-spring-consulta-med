package med.voll.api.consulta;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import med.voll.api.consulta.validacoes.agendamento.*;
import med.voll.api.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
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
	
	@Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;
	
	
	public AgendaDeConsultas() {
		super();
	}

	public AgendaDeConsultas(ConsultaRepository consultaRepository, MedicoRepository medicoRepository,
			PacienteRepository pacienteRepository, List<ValidadorAgendamentoDeConsulta> validadores,
			List<ValidadorCancelamentoDeConsulta> validadoresCancelamento) {
		super();
		this.consultaRepository = consultaRepository;
		this.medicoRepository = medicoRepository;
		this.pacienteRepository = pacienteRepository;
		this.validadores = validadores;
		this.validadoresCancelamento = validadoresCancelamento;
	}

	public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {

		if (!pacienteRepository.existsById(dados.idPaciente())) {
			throw new ValidacaoException("Id do paciente informado não existe!");

		}
		if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
			throw new ValidacaoException("Id do médico informado não existe!");
		}
		
		validadores.forEach(v -> v.validar(dados));

		var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
		var medico = escolherMedico(dados);
		
		if(medico == null) {
			throw new ValidacaoException("Não existe medico disponivel nessa data!");
		}

		var consulta = new Consulta(null, medico, paciente, dados.data());
		consultaRepository.save(consulta);
		
		return new DadosDetalhamentoConsulta(consulta);
	}
	
	public Consulta cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
        
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
	
	public Page<DadosDetalhamentoConsulta> buscarTodos(Pageable paginacao) {
		return consultaRepository.findAll(paginacao).map(DadosDetalhamentoConsulta::new);
	}
}
