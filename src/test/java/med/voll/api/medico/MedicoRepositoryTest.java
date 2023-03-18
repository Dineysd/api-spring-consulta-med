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
import med.voll.api.dados.MedicoBuilder;
import med.voll.api.dados.PacienteBuilder;
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
        var medico = cadastrarMedico(true);
        var paciente = cadastrarPaciente();
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
        var medico = cadastrarMedico(true);

        //when ou act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);

        //then ou assert
        assertThat(medicoLivre).isEqualTo(medico);
    }
	
	@Test
    @DisplayName("Deve retornar verdadeiro caso medico esteja ativo")
    void findAllByAtivoTrue() {
        //given ou arranges
		
        var medico = cadastrarMedico(true);
       
        //when ou act
        var medicoLivre = medicoRepository.findAtivoById(medico.getId());

        //then ou assert
        assertThat(medicoLivre).isTrue();
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
	
	private Paciente cadastrarPaciente() {
        var paciente = new PacienteBuilder().comNome()
        		.comEmail()
    			.comCpf()
    			.comTelefone()
    			.comEndereco()
    			.builder();
        em.persist(paciente);
        return paciente;
    }
	
	private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
        em.persist(new Consulta(null, medico, paciente, data));
    }
	

}
