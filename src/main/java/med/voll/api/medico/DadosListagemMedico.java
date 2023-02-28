package med.voll.api.medico;

import med.voll.api.model.Especilidade;
import med.voll.api.model.Medico;

public record DadosListagemMedico(String nome, String email, String crm, Especilidade especialidade) {

    public DadosListagemMedico(Medico medico) {
        this(medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }

}
