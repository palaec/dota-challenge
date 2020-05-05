package gg.bayes.challenge.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table
public class HeroDamageData{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -4009184752242247464L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;
	@Column
	@NotNull
	private Long matchId;
	@Column
	@NotNull
	private Long timestamp;
	@Column
	@NotNull
	private String hero;
	@Column
	@NotNull
	private Integer damage;
	@Column
	@NotNull
	private String target;
	@Column
	@NotNull
	private String attackWith;
	@Column
	@NotNull
	private String attackType;
}
