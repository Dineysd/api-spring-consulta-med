package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;
import med.voll.api.model.Especilidade;

public record DadosCadastroMedico(@NotBlank String nome,
		@NotBlank 
		@Email() 
		String email,
		@NotBlank 
		@Pattern(regexp = "\\d{4,6}") 
		String crm,
		@NotBlank
        String telefone,
		@NotNull
		Especilidade especialidade,
		@NotNull
		@Valid
		DadosEndereco endereco) {
	
//@NotBlank -- só para campos String

}
