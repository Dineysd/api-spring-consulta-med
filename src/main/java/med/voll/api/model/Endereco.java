package med.voll.api.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.DadosEndereco;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
	
	private String logradouro; 
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	
	public Endereco() {}
	
	public Endereco(DadosEndereco endereco) {
		this.logradouro = endereco.logradouro();
		this.numero = endereco.numero();
		this.complemento = endereco.complemento();
		this.bairro = endereco.bairro();
		this.cidade = endereco.cidade();
		this.uf = endereco.uf();
		this.cep = endereco.cep();
	}

}
