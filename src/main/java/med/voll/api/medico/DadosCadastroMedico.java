package med.voll.api.medico;

import med.voll.api.endereco.DadosEndereco;
import med.voll.api.model.Especilidade;

public record DadosCadastroMedico(String nome, String email, String crm, Especilidade especialidade, DadosEndereco endereco) {

}
