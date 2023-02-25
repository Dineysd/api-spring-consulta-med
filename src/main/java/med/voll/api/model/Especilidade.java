package med.voll.api.model;

public enum Especilidade {
	
	Ortopedia("ortopedia"), 
	Cardiologia("cardiologia"), 
	Ginecologia("ginecologia"), 
	Dermatologia("dermatologia");
	
	private String descricao;

	Especilidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
