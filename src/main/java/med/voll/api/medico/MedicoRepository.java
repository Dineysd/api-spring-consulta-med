package med.voll.api.medico;

import org.springframework.data.jpa.repository.JpaRepository;

import med.voll.api.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long>{

}
