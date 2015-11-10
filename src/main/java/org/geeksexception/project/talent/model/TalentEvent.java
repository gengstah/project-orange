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
	
	@Column(name = "RATING")
	private Integer rating;
	
	@Column(name = "FEEDBACK")
	private String feedback;

	public TalentEvent() { }
	
	public TalentEvent(Talent talent, Event event) { this.talent = talent; this.event = event; }
	
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
	
	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((talent == null) ? 0 : talent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TalentEvent other = (TalentEvent) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (talent == null) {
			if (other.talent != null)
				return false;
		} else if (!talent.equals(other.talent))
			return false;
		return true;
	}
	
}