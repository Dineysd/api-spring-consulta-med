package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import med.voll.api.dados.EnderecoBuilder;
import med.voll.api.dados.PacienteBuilder;
import med.voll.api.model.Endereco;
import med.voll.api.model.Paciente;
import med.voll.api.paciente.DadosAtualizarPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosDetalhesPaciente;
import med.voll.api.paciente.PacienteRepository;



@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PacienteControllerTest {
	
	@Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroPaciente> dadosCadastroJson;
    
    @Autowired
    private JacksonTester<DadosAtualizarPaciente> dadosAtualizaPacienteJson;

    @Autowired
    private JacksonTester<DadosDetalhesPaciente> dadosDetalhesJson;
    
    @MockBean
	private PacienteRepository repository;
    
    @Test
    @DisplayName("Deve devolver codigo http 400 quando informacoes do paciente estao invalidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc
                .perform(post("/pacientes"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
    	var dadosCadastro = new DadosCadastroPaciente("paciente",
				"paciente@gmail.com",
				"05941772947",
				"988502857",
				new EnderecoBuilder().comNumero().comComplemento().dadosDtoBuilder()) ;
    	
    	var paciente = new Paciente(dadosCadastro);
    	//paciente.setId(1L);

        when(repository.save(any())).thenReturn(paciente);

        var response = mvc
                .perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroJson.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhesPaciente(
                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.cpf(),
                dadosCadastro.telefone(),
                new Endereco(dadosCadastro.endereco())
        );
        var jsonEsperado = dadosDetalhesJson.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

}
