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
public class HeroKillsData{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -6339187752242241464L;
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
	private String target;
 
}
