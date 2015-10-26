package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "TALENT")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = Talent.class)
public class Talent implements Serializable {
	
	private static final long serialVersionUID = 6866469865603303087L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "TALENT_ID", nullable = false)
	private Long id;
	
	@Column(name = "FIRSTNAME", nullable = false)
	@NotEmpty(message = "firstName must not be empty")
	private String firstName;

	@Column(name = "LASTNAME", nullable = false)
	@NotEmpty(message = "lastName must not be empty")
	private String lastName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "BIRTHDATE", nullable = false)
	@NotNull(message = "birthDate should not be null")
	private Date birthDate;
	
	private Integer feet;
	
	private Integer inches;
	
	private BigDecimal weight;
	
	public Talent() { }
	
}