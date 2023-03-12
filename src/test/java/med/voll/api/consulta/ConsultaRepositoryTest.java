package med.voll.api.consulta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import med.voll.api.dados.DadosParaTest;
import med.voll.api.model.Consulta;
import med.voll.api.model.Especialidade;
import med.voll.api.model.Medico;
import med.voll.api.model.Paciente;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ConsultaRepositoryTest {
	
	
	@Autowired
	private ConsultaRepository consultaRepositiry;
	
	@Autowired
    private TestEntityManager em;
	
	@Test
	@DisplayName("Deve retornar verdadeiro caso exista paciente com consulta agendada nesse dia")
	public void existsByPacienteIdAndDataBetween() {
		
		var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var dado = new DadosParaTest();
        var medico = cadastrarMedico(dado, "Medico", "medico@voll.med", "92345", Especialidade.CARDIOLOGIA, true);
        var paciente = cadastrarPaciente(dado, "Paciente", "paciente@email.com", "00000000000");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10, null);
        
		
		var primeiroHorario = proximaSegundaAs10.withHour(7);
		var ultimoHorario = proximaSegundaAs10.withHour(18);
		var pacientePossuiConsultaNesseDia = consultaRepositiry.existsByPacienteIdAndDataBetween(paciente.getId(), primeiroHorario, ultimoHorario);
		
		assertThat(pacientePossuiConsultaNesseDia).isTrue();
	}
	
	@Test
	@DisplayName("Deve retornar falso caso exista consulta cancelada")
	public void existsByMedicoIdAndDataAndMotivoCancelamentoIsNull() {
		
		var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var dado = new DadosParaTest();
        var medico = cadastrarMedico(dado, "Medico", "medico@voll.med", "92345", Especialidade.CARDIOLOGIA, true);
        var paciente = cadastrarPaciente(dado, "Paciente", "paciente@email.com", "00000000000");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10, MotivoCancelamento.MEDICO_CANCELOU);
        
		var isConsultaCancelada = consultaRepositiry.existsByMedicoIdAndDataAndMotivoCancelamentoIsNull(medico.getId(), proximaSegundaAs10);
		
		assertThat(isConsultaCancelada).isFalse();
	}
	
	private Medico cadastrarMedico(DadosParaTest dado, String nome, String email, String crm, Especialidade especialidade, boolean ativo) {
        var medico = new Medico(dado.dadosMedico(nome, email, crm, especialidade));
        if(!ativo) {
        	medico.desativar();
        }
        em.persist(medico);
        return medico;
    }
	
	private Paciente cadastrarPaciente(DadosParaTest dado, String nome, String email, String cpf) {
        var paciente = new Paciente(dado.dadosPaciente(nome, email, cpf));
        em.persist(paciente);
        return paciente;
    }
	
	private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data, MotivoCancelamento motivo) {
		var consulta = new Consulta(null, medico, paciente, data);
		consulta.cancelar(motivo);
		em.persist(consulta);
    }

}
