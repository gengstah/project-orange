package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "TALENT_EVENT")
@IdClass(TalentEventId.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = TalentEvent.class)
public class TalentEvent implements Serializable {
	
	private static final long serialVersionUID = -8559011030381547697L;

	@Id
	@ManyToOne
	@JoinColumn(name = "TALENT_ID")
	@NotNull(message = "Talent must not be null")
	private Talent talent;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "EVENT_ID")
	@NotNull(message = "Event must not be null")
	private Event event;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED")
	private Date dateCreated;
	
	@Column(name = "SUCCESSFUL")
	@NotNull(message = "Successful field must not be null")
	private Boolean successful;
	
	public TalentEvent() { }
	
	@PrePersist
	public void prePersist() {
		dateCreated = new Date();
	}

	public Talent getTalent() {
		return talent;
	}

	public void setTalent(Talent talent) {
		this.talent = talent;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}
	
}