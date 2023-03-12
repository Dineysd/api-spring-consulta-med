package med.voll.api.medico;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import med.voll.api.dados.DadosParaTest;
import med.voll.api.model.Consulta;
import med.voll.api.model.Especialidade;
import med.voll.api.model.Medico;
import med.voll.api.model.Paciente;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MedicoRepositoryTest {
	
	@Autowired
    private MedicoRepository medicoRepository;
	
	@Autowired
    private TestEntityManager em;
	
	@Test
    @DisplayName("Deve retornar null quando o unico medico cadastrado nao esta disponivel na data")
    void escolherMedicoAleatorioLivreNaDataCenario1() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var dado = new DadosParaTest();
        var medico = cadastrarMedico(dado, "Medico", "medico@voll.med", "92345", Especialidade.CARDIOLOGIA, true);
        var paciente = cadastrarPaciente(dado, "Paciente", "paciente@email.com", "00000000000");
        cadastrarConsulta(medico, paciente, proximaSegundaAs10);

        //when ou act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        //then ou assert
        assertThat(medicoLivre).isNull();
    }
	
	@Test
    @DisplayName("Deve retornar medico quando tiver disponivel")
    void escolherMedicoAleatorioLivreNaDataCenario2() {
        //given ou arrange
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10, 0);
        var medico = cadastrarMedico(new DadosParaTest(), "Medico", "medico@voll.med", "92345", Especialidade.CARDIOLOGIA, true);

        //when ou act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        //then ou assert
        assertThat(medicoLivre).isEqualTo(medico);
    }
	
	@Test
    @DisplayName("Deve retornar verdadeiro caso medico esteja ativo")
    void findAllByAtivoTrue() {
        //given ou arranges
		
        var medico = cadastrarMedico(new DadosParaTest(), "Medico", "medico@voll.med", "92345", Especialidade.CARDIOLOGIA, true);
       
        //when ou act
        var medicoLivre = medicoRepository.findAtivoById(medico.getId());

        //then ou assert
        assertThat(medicoLivre).isTrue();
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
	
	private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        em.persist(new Consulta(null, medico, paciente, data));
    }
	

}
