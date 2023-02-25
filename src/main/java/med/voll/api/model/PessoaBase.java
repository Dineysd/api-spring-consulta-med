package med.voll.api.model;

import java.util.Objects;

public abstract class PessoaBase {
	
	private Integer id;
	private String nome;
	private String email;
	private String telefone;
	private Endereco endereco;
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaBase other = (PessoaBase) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
	

}
