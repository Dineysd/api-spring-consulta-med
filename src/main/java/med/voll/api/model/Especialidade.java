package med.voll.api.model;

public enum Especialidade {
	
	ORTOPEDIA("ortopedia"), 
	CARDIOLOGIA("cardiologia"), 
	GINECOLOGISTA("ginecologia"), 
	DERMATOLOGISTA("dermatologia");
	
	private String descricao;

	Especialidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
