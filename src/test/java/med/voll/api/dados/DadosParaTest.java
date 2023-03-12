package med.voll.api.dados;

import med.voll.api.endereco.DadosEndereco;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.model.Especialidade;
import med.voll.api.paciente.DadosCadastroPaciente;


public class DadosParaTest {
	
	
	public DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
        return new DadosCadastroPaciente(
                nome,
                email,
                "61999999999",
                cpf,
                dadosEndereco()
        );
    }
	
	
	public DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
        return new DadosCadastroMedico(
                nome,
                email,
                crm,
                "61999999999",
                especialidade,
                dadosEndereco()
        );
    }
	
	
	private DadosEndereco dadosEndereco() {
        return new DadosEndereco("rua xpto",
        		null,
        		null,
                "bairro",
                "Maringá",
                "DF",
                "00000000"
        );
    }


}
