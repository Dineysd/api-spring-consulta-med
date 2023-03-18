package med.voll.api.consulta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import med.voll.api.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import med.voll.api.consulta.validacoes.agendamento.ValidadorHorarioAntecedencia;
import med.voll.api.consulta.validacoes.agendamento.ValidadorHorarioFuncionamentoClinica;
import med.voll.api.consulta.validacoes.agendamento.ValidadorMedicoAtivo;
import med.voll.api.consulta.validacoes.agendamento.ValidadorMedicoComOutraConsultaNoMesmoHorario;
import med.voll.api.consulta.validacoes.agendamento.ValidadorPacienteAtivo;
import med.voll.api.consulta.validacoes.agendamento.ValidadorPacienteSemOutraConsultaNoDia;
import med.voll.api.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import med.voll.api.dados.DadosParaTest;
import med.voll.api.dados.MedicoBuilder;
import med.voll.api.dados.PacienteBuilder;
import med.voll.api.infra.exception.ValidacaoException;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.model.Consulta;
import med.voll.api.model.Especialidade;
import med.voll.api.model.Medico;
import med.voll.api.model.Paciente;
import med.voll.api.paciente.PacienteRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AgendaDeConsultasTest {
	
	@Autowired
    private TestEntityManager em;
	
	@Autowired
	private ConsultaRepository repoConsulta;

	@Autowired
	private MedicoRepository repoMedico;

	@Autowired
	private PacienteRepository repoPaciente;
	
	
	@Test
	@DisplayName("Deve retornar Exception validacão de Id do paciente informado não existe")
	public void cenario_agendamento_01() {
		var agenda = new AgendaDeConsultas(null,null,repoPaciente,null,null);
		var data = LocalDateTime.now();
		
		var dadosDetalhamento = new DadosAgendamentoConsulta( 2l, 0l,data, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			assertEquals("Id do paciente informado não existe!", ex.getMessage());
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de Id do médico informado não existe")
	public void cenario_agendamento_02() {
		var agenda = new AgendaDeConsultas(null,repoMedico,repoPaciente,null,null);
		var data = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
		var dado = new DadosParaTest();
		var paciente = cadastrarPaciente(true);
		cadastrarMedico(true);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta( 2l, paciente.getId(),data, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			assertEquals("Id do médico informado não existe!", ex.getMessage());
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de agendamento com antecedencia")
	public void cenario_agendamento_03() {
		List<ValidadorAgendamentoDeConsulta> validadores = new ArrayList<ValidadorAgendamentoDeConsulta>();
		validadores.add(new ValidadorHorarioAntecedencia());
		var agenda = new AgendaDeConsultas(repoConsulta,repoMedico,repoPaciente,validadores,null);
		var data10minutos = LocalDateTime.now().plusMinutes(10);
		var dado = new DadosParaTest();
		var paciente = cadastrarPaciente(true);
		var medico = cadastrarMedico(true);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(),data10minutos, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			var message = ex.getMessage();
			assertEquals("Consulta deve ser agendada com antecedência mínima de 30 minutos", message);
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de agendamento fora do horário")
	public void cenario_agendamento_04() {
		List<ValidadorAgendamentoDeConsulta> validadores = new ArrayList<ValidadorAgendamentoDeConsulta>();
		validadores.add(new ValidadorHorarioFuncionamentoClinica());
		var agenda = new AgendaDeConsultas(repoConsulta,repoMedico,repoPaciente,validadores,null);
		var dataDomingo10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                .atTime(10, 0);
		var dado = new DadosParaTest();
		var paciente = cadastrarPaciente(true);
		var medico = cadastrarMedico(true);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(),dataDomingo10, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			var message = ex.getMessage();
			assertEquals("Consulta fora do horário de funcionamento da clínica", message);
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de medico removido")
	public void cenario_agendamento_05() {
		List<ValidadorAgendamentoDeConsulta> validadores = new ArrayList<ValidadorAgendamentoDeConsulta>();
		validadores.add(new ValidadorMedicoAtivo(repoMedico));
		var agenda = new AgendaDeConsultas(repoConsulta,repoMedico,repoPaciente,validadores,null);
		var dataSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
		var dado = new DadosParaTest();
		var paciente = cadastrarPaciente(true);
		var medico = cadastrarMedico(false);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(),dataSegundaAs10, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			var message = ex.getMessage();
			assertEquals("Consulta não pode ser agendada com o medico removido", message);
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de medico com outra consulta agendada")
	public void cenario_agendamento_06() {
		List<ValidadorAgendamentoDeConsulta> validadores = new ArrayList<ValidadorAgendamentoDeConsulta>();
		validadores.add(new ValidadorMedicoComOutraConsultaNoMesmoHorario(repoConsulta));
		var agenda = new AgendaDeConsultas(repoConsulta,repoMedico,repoPaciente,validadores,null);
		var dataSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
		var paciente = cadastrarPaciente(true);
		var medico = cadastrarMedico(true);
		cadastrarConsulta(medico, paciente, dataSegundaAs10, null);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(),dataSegundaAs10, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			var message = ex.getMessage();
			assertEquals("Medico ja possui outra consulta agendada nesse mesmo horario", message);
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de paciente removido")
	public void cenario_agendamento_07() {
		List<ValidadorAgendamentoDeConsulta> validadores = new ArrayList<ValidadorAgendamentoDeConsulta>();
		validadores.add(new ValidadorPacienteAtivo(repoPaciente));
		var agenda = new AgendaDeConsultas(repoConsulta,repoMedico,repoPaciente,validadores,null);
		var dataSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
		var paciente = cadastrarPaciente(false);
		var medico = cadastrarMedico(true);
		cadastrarConsulta(medico, paciente, dataSegundaAs10, null);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(),dataSegundaAs10, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			var message = ex.getMessage();
			assertEquals("Consulta não pode ser agendada com paciente removido", message);
		}
		
	}
	
	@Test
	@DisplayName("Deve retornar Exception validacão de paciente com consulta agendada nesse dia")
	public void cenario_agendamento_08() {
		List<ValidadorAgendamentoDeConsulta> validadores = new ArrayList<ValidadorAgendamentoDeConsulta>();
		validadores.add(new ValidadorPacienteSemOutraConsultaNoDia(repoConsulta));
		var agenda = new AgendaDeConsultas(repoConsulta,repoMedico,repoPaciente,validadores,null);
		var dataSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
		var paciente = cadastrarPaciente(false);
		var medico = cadastrarMedico(true);
		cadastrarConsulta(medico, paciente, dataSegundaAs10, null);
		
		var dadosDetalhamento = new DadosAgendamentoConsulta(medico.getId(), paciente.getId(),dataSegundaAs10, Especialidade.CARDIOLOGIA );
		
		try {
			agenda.agendar(dadosDetalhamento);
			fail("deve falhar");
		}catch(ValidacaoException ex) {
			var message = ex.getMessage();
			assertEquals("Paciente já possui uma consulta agendada nesse dia!", message);
		}
		
	}
	
	private Medico cadastrarMedico(boolean ativo) {
        var medico = new MedicoBuilder().comNome()
        		.comEmail()
    			.comCrm()
    			.comTelefone()
    			.comEspecialidade()
    			.comEndereco()
    			.builder();
        
        if(!ativo) {
        	medico.desativar();
        }
        em.persist(medico);
        return medico;
    }
	
	private Paciente cadastrarPaciente(boolean ativo) {
        var paciente = new PacienteBuilder().comNome()
        		.comEmail()
    			.comCpf()
    			.comTelefone()
    			.comEndereco()
    			.builder();
        
        if(!ativo) {
        	paciente.desativar();
        }
        em.persist(paciente);
        return paciente;
    }
	private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data, MotivoCancelamento motivo) {
		var consulta = new Consulta(null, medico, paciente, data);
		if(motivo != null) {
		consulta.cancelar(motivo);
		}
		em.persist(consulta);
    }

}
