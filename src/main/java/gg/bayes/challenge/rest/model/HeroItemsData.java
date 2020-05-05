package gg.bayes.challenge.rest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter 
@Entity
@Table
public class HeroItemsData extends HeroItems{
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -8009167732242241326L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@JsonIgnore
	private Long id;
	@Column
	@NotNull
	@JsonIgnore
	private Long matchId;
	@Column
	@NotNull
	private Long timestamp;
	@Column
	@NotNull
	@JsonIgnore
	private String hero;
	@Column
	@NotNull
	private String item;  
}
