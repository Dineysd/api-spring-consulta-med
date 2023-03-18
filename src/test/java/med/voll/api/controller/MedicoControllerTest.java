package med.voll.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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

import med.voll.api.dados.MedicoBuilder;
import med.voll.api.medico.DadosAtualizarMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.DadosDetalhamentoMedico;
import med.voll.api.medico.MedicoRepository;
import med.voll.api.model.Endereco;
import med.voll.api.model.Medico;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class MedicoControllerTest {
	
	@Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DadosCadastroMedico> dadosCadastroMedicoJson;
    
    @Autowired
    private JacksonTester<DadosAtualizarMedico> dadosAtualizaMedicoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoMedico> dadosDetalhamentoMedicoJson;

    @MockBean
    private MedicoRepository repository;
    
    @Test
    @DisplayName("Deve devolver codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc
                .perform(post("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    @DisplayName("Deve devolver codigo http 201 quando informacoes estao validas")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
    	var dadosCadastro = new MedicoBuilder()
    			.comNome()
    			.comEmail()
    			.comCrm()
    			.comTelefone()
    			.comEspecialidade()
    			.comDadosEndereco()
    			.dadosBuilder();

        when(repository.save(any())).thenReturn(new Medico(dadosCadastro));

        var response = mvc
                .perform(post("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosCadastroMedicoJson.write(dadosCadastro).getJson()))
                .andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhamentoMedico(
                null,
                dadosCadastro.nome(),
                dadosCadastro.email(),
                dadosCadastro.crm(),
                dadosCadastro.telefone(),
                dadosCadastro.especialidade(),
                new Endereco(dadosCadastro.endereco())
        );
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    
    @Test
    @DisplayName("Deve devolver codigo http 200 quando informacoes forem alteradas")
    @WithMockUser
    void cadastrar_cenario3() throws Exception {
    	var atualizaCadastro = new MedicoBuilder()
    			.comCodigo()
    			.comNovoNome("Heloisa")
    			.comTelefone()
    			.comDadosEndereco()
    			.atualizarBuilder();
    	
    	var dadosCompletos = new MedicoBuilder()
    			.comNovoNome("Heloisa")
    			.comCrm()
    			.comEmail()
    			.comTelefone()
    			.comDadosEndereco()
    			.dadosBuilder();
    	
    	var medico = new Medico(dadosCompletos);
    	medico.setId(atualizaCadastro.id());

        when(repository.getReferenceById(atualizaCadastro.id())).thenReturn(medico);
        medico.desativar();

        var response = mvc
                .perform(put("/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAtualizaMedicoJson.write(atualizaCadastro).getJson()))
                .andReturn().getResponse();

        var dadosDetalhamento = new DadosDetalhamentoMedico(
                atualizaCadastro.id(),
                dadosCompletos.nome(),
                dadosCompletos.email(),
                dadosCompletos.crm(),
                dadosCompletos.telefone(),
                dadosCompletos.especialidade(),
                new Endereco(dadosCompletos.endereco())
        );
        var jsonEsperado = dadosDetalhamentoMedicoJson.write(dadosDetalhamento).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    
    @Test
    @DisplayName("Deve devolver codigo http 204 quando deativar vendedor")
    @WithMockUser
    void cadastrar_cenario4() throws Exception {
    	var dadosCompletos = new MedicoBuilder()
    			.comNovoNome("Heloisa")
    			.comCrm()
    			.comEmail()
    			.comTelefone()
    			.comDadosEndereco()
    			.dadosBuilder();
    	
    	var medico = new Medico(dadosCompletos);
    	medico.setId(1L);

        when(repository.getReferenceById(1L)).thenReturn(medico);
        medico.desativar();
        
        var response = mvc
                .perform(delete("/medicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
