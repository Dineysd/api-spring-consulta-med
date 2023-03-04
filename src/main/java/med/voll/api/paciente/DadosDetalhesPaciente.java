package med.voll.api.paciente;

import med.voll.api.model.Endereco;
import med.voll.api.model.Paciente;

public record DadosDetalhesPaciente(Long id, String nome, String email, String cpf, String telefone, Endereco endereco) {
	public DadosDetalhesPaciente(Paciente paciente) {
		this(paciente.getId(),paciente.getNome(),paciente.getEmail(), paciente.getCpf(), paciente.getTelefone(), paciente.getEndereco());
	}
}
