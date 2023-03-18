package med.voll.api.dados;

import med.voll.api.endereco.DadosEndereco;
import med.voll.api.model.Endereco;

public class EnderecoBuilder {
	
	private String logradouro; 
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	
	public EnderecoBuilder() {
		this.logradouro = "rua xpto";
		this.bairro = "bairro";
		this.cidade = "Maringa";
		this.uf = "PR";
		this.cep = "00000000";
	}
	
	public EnderecoBuilder comNumero() {
		this.numero = "10";
		return this;
	}



	public EnderecoBuilder comComplemento() {
		this.complemento = "casa amarela";
		return this;
	}
	
	
	
	public Endereco completoBuilder() {
		return new Endereco(logradouro, numero, complemento,bairro,cidade,uf,cep);
	}
	

	public DadosEndereco dadosDtoBuilder() {
		// TODO Auto-generated method stub
		return new DadosEndereco(logradouro,
				numero,
				complemento,
				bairro,
				cidade,
				uf,
				cep
        );
	}


}
