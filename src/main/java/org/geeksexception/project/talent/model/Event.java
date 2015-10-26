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

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "EVENT")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = Event.class)
public class Event implements Serializable {
	
	private static final long serialVersionUID = 3238561696427818834L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "EVENT_ID", nullable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false)
	@NotEmpty(message = "Event name must not be empty")
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(name = "RUN_DATE_FROM", nullable = true)
	private Date runDateFrom;
	
	@Column(name = "RUN_DATE_TO", nullable = true)
	private Date runDateTo;
	
	@Column(name = "TALENT_FEE", nullable = true)
	private BigDecimal talentFee;
	
	public Event() { }
}