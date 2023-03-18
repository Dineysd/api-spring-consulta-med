package med.voll.api.dados;


import med.voll.api.endereco.DadosEndereco;
import med.voll.api.model.Endereco;
import med.voll.api.model.Paciente;
import med.voll.api.paciente.DadosCadastroPaciente;

public class PacienteBuilder {
	
	private Long id;
	private String nome;
	private String email;
	private String cpf;
	private String telefone;
	private Endereco endereco;
	private DadosEndereco dadosEndereco;
	private Boolean ativo;
	
	public PacienteBuilder comCodigo() {
		this.id = 1L;
		return this;
	}
	public PacienteBuilder comNome() {
		this.nome = "Henrique Benício Antonio Pires";
		return this;
	}
	public PacienteBuilder comEmail() {
		this.email = "henrique_pires@zepto.com.br";
		return this;
	}
	public PacienteBuilder comCpf() {
		this.cpf = "95550922908";
		return this;
	}
	public PacienteBuilder comTelefone() {
		this.telefone = "987254321";
		return this;
	}
	
	public PacienteBuilder comDadosEndereco() {
		this.dadosEndereco =  new EnderecoBuilder().dadosDtoBuilder();
		return this;
	}
	
	public PacienteBuilder comEndereco() {
		this.endereco = new EnderecoBuilder().completoBuilder();
		return this;
	}
	
	public DadosCadastroPaciente dadosBuilder() {
		return new DadosCadastroPaciente(nome,
				email,
				cpf,
				telefone,
				dadosEndereco) ;
	}
	
	public Paciente builder() {
		return new Paciente(nome,
				email,
				cpf,
				telefone,
				endereco) ;
	}

}
