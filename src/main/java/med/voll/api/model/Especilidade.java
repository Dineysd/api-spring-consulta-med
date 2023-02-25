package med.voll.api.model;

public enum Especilidade {
	
	ORTOPEDIA("ortopedia"), 
	CARDIOLOGIA("cardiologia"), 
	GINECOLOGISTA("ginecologia"), 
	DERMATOLOGISTA("dermatologia");
	
	private String descricao;

	Especilidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
