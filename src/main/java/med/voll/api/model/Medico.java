package med.voll.api.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.medico.DadosAtualizarMedico;
import med.voll.api.medico.DadosCadastroMedico;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String crm;
	private String telefone;
	@Enumerated(EnumType.STRING)
	private Especialidade especialidade;
	@Embedded
	private Endereco endereco;
	private Boolean ativo;
	

	//public Medico() {}

	public Medico(DadosCadastroMedico dados) {
		this.ativo = true;
		this.nome = dados.nome();
		this.email = dados.email();
		this.crm = dados.crm();
		this.telefone = dados.telefone();
		this.especialidade = dados.especialidade();
		this.endereco = new Endereco(dados.endereco());
		}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

	public String getCrm() {
		return crm;
	}

	public String getTelefone() {
		return telefone;
	}

	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public Endereco getEndereco() {
		return endereco;
	}
	
	public void atualizarInformacoes(DadosAtualizarMedico dados) {
	    if (dados.nome() != null) {
	        this.nome = dados.nome();
	    }
	    if (dados.telefone() != null) {
	        this.telefone = dados.telefone();
	    }
	    if (dados.endereco() != null) {
	        this.endereco.atualizarInformacoes(dados.endereco());
	    }

	}

	public void desativar() {
		this.ativo = false;
	}

	public Medico(String nome2, String email2, String crm2, String telefone2, Especialidade especialidade2,
			Endereco endereco2) {
		this.ativo = true;
		this.nome = nome2;
		this.email = email2;
		this.crm = crm2;
		this.telefone = telefone2;
		this.especialidade = especialidade2;
		this.endereco = endereco2;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

