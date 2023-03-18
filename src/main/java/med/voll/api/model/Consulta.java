package med.voll.api.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.consulta.MotivoCancelamento;

@Table(name = "consultas")
@Entity(name = "Consulta")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Consulta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medico_id")
	private Medico medico;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "paciente_id")
	private Paciente paciente;

	@NotNull
	@Future
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime data;

	@Column(name = "motivo_cancelamento")
    @Enumerated(EnumType.STRING)
	private MotivoCancelamento motivoCancelamento;

	public Consulta(Long id, Medico medico, Paciente paciente, @NotNull @Future LocalDateTime data ) {
		super();
		this.id = id;
		this.medico = medico;
		this.paciente = paciente;
		this.data = data;
	}
	
	
	
	
	public void cancelar(MotivoCancelamento motivo) {
		this.motivoCancelamento = motivo;
	}
	
	

}
