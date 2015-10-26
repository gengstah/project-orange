package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@ManyToOne
	@JoinColumn(name="AGENCY_ID")
	private Agency agency;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false)
	private Date dateCreated;
	
	public Event() { }
	
	@PrePersist
	public void prePersist() {
		dateCreated = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getRunDateFrom() {
		return runDateFrom;
	}

	public void setRunDateFrom(Date runDateFrom) {
		this.runDateFrom = runDateFrom;
	}

	public Date getRunDateTo() {
		return runDateTo;
	}

	public void setRunDateTo(Date runDateTo) {
		this.runDateTo = runDateTo;
	}

	public BigDecimal getTalentFee() {
		return talentFee;
	}

	public void setTalentFee(BigDecimal talentFee) {
		this.talentFee = talentFee;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
}