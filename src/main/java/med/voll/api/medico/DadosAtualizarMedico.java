package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.api.endereco.DadosEndereco;

public record DadosAtualizarMedico(@NotNull Long id, @NotBlank String nome,
		@NotBlank
        String telefone,
		@NotNull
		@Valid
		DadosEndereco endereco) {

}
