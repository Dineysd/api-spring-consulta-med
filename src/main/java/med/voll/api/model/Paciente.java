package med.voll.api.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.paciente.DadosAtualizarPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;


@Table(name = "pacientes")
@Entity(name = "Paciente")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String telefone;
	@Embedded
	private Endereco endereco;
	private Boolean ativo;
	
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getCpf() {
		return cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	//public Paciente(){}
	
	public Paciente(DadosCadastroPaciente dados) {
		this.ativo = true;
		this.nome = dados.nome();
		this.email = dados.email();
		this.cpf = dados.cpf();
		this.telefone = dados.telefone();
		this.endereco = new Endereco(dados.endereco());
		}

	public void atualizarInformacoes(DadosAtualizarPaciente dados) {
		if (dados.nome() != null) {
	        this.nome = dados.nome();
	    }
	    if (dados.telefone() != null) {
	        this.telefone = dados.telefone();
	    }
	    if (dados.endereco() != null) {
	        this.endereco.atualizarInformacoes(dados.endereco());
	    }
	    
	    if(dados.ativo() != null && dados.ativo()) {
	    	this.ativo = dados.ativo();
	    }
		
	}

	public void desativar() {
		this.ativo = false;
	}

}
