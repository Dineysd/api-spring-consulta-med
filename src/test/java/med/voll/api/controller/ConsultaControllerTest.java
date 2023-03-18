package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

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

import med.voll.api.consulta.AgendaDeConsultas;
import med.voll.api.consulta.DadosAgendamentoConsulta;
import med.voll.api.consulta.DadosCancelamentoConsulta;
import med.voll.api.consulta.DadosDetalhamentoConsulta;
import med.voll.api.consulta.MotivoCancelamento;
import med.voll.api.model.Consulta;
import med.voll.api.model.Especialidade;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ConsultaControllerTest {
	
	@Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosAgendamentoConsulta> dadosAgendamentoConsultaJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoConsulta> dadosDetalhamentoConsultaJson;
    
    @Autowired
    private JacksonTester<DadosCancelamentoConsulta> dadosCancelamentoConsultaJson;

    @MockBean
    private AgendaDeConsultas agendaDeConsultas;
    
    @Test
    @DisplayName("Deveria devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void agendar_cenario1() throws Exception {
        var response = mvc.perform(post("/consultas"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void agendar_cenario2() throws Exception {
        var data = LocalDateTime.now().plusHours(1);
        var especialidade = Especialidade.CARDIOLOGIA;

        var dadosDetalhamento = new DadosDetalhamentoConsulta(null, 2l, 5l, data);
        
        when(agendaDeConsultas.agendar(any())).thenReturn(dadosDetalhamento);

        var response = mvc
                .perform(
                        post("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosAgendamentoConsultaJson.write(
                                        new DadosAgendamentoConsulta(2l, 5l, data, especialidade)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = dadosDetalhamentoConsultaJson.write(
                dadosDetalhamento
        ).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    
    @Test
    @DisplayName("Deve devolver 204 quando informacoes for validas")
    @WithMockUser
    void agendar_cenario3() throws Exception {

        Consulta consulta = new Consulta();
        when(agendaDeConsultas.cancelar(any())).thenReturn(consulta);

        var response = mvc
                .perform(
                        delete("/consultas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dadosCancelamentoConsultaJson.write(
                                		new DadosCancelamentoConsulta(1l, MotivoCancelamento.MEDICO_CANCELOU)).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        
    }

}
