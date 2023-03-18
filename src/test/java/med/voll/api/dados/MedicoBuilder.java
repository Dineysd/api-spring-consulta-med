package med.voll.api.dados;


import med.voll.api.endereco.DadosEndereco;
import med.voll.api.medico.DadosAtualizarMedico;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.model.Endereco;
import med.voll.api.model.Especialidade;
import med.voll.api.model.Medico;

public class MedicoBuilder {
	
	private Long id;
	private String nome;
	private String email;
	private String crm;
	private String telefone;
	private Especialidade especialidade;
	private Endereco endereco;
	private DadosEndereco dadosEndereco;
	private Boolean ativo;
	
	public MedicoBuilder comCodigo() {
		this.id = 1L;
		return this;
	}
	
	public MedicoBuilder comNovoNome(String nome) {
		this.nome = nome;
		return this;
	}
	public MedicoBuilder comNome() {
		this.nome = "Carlos";
		return this;
	}
	public MedicoBuilder comEmail() {
		this.email = "medico@voll.med";
		return this;
	}
	public MedicoBuilder comCrm() {
		this.crm = "43536";
		return this;
	}
	public MedicoBuilder comTelefone() {
		this.telefone = "987654321";
		return this;
	}
	public MedicoBuilder comEspecialidade() {
		this.especialidade = Especialidade.CARDIOLOGIA;
		return this;
	}
	public MedicoBuilder comDadosEndereco() {
		this.dadosEndereco =  new EnderecoBuilder().dadosDtoBuilder();
		return this;
	}
	
	public MedicoBuilder comEndereco() {
		this.endereco = new EnderecoBuilder().completoBuilder();
		return this;
	}
	public MedicoBuilder inativo() {
		this.ativo = false;
		return this;
	}
	
	public DadosCadastroMedico dadosBuilder() {
		return new DadosCadastroMedico(nome,
				email,
				crm,
				telefone,
				especialidade,
				dadosEndereco) ;
	}
	
	public DadosAtualizarMedico atualizarBuilder() {
		return new DadosAtualizarMedico(id, nome,
				telefone,
				dadosEndereco) ;
	}
	
	public Medico builder() {
		return new Medico(nome,
				email,
				crm,
				telefone,
				especialidade,
				endereco) ;
	}
	
	
	
	

}
