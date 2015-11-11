package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.geeksexception.project.talent.enums.EventStatus;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "EVENT")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = Event.class)
public class Event implements Serializable {
	
	private static final long serialVersionUID = 3238561696427818834L;
	
	private static final String FOR_APPROVAL_NOTE = "Your event is still for approval. Please allow us to process your request within 24-48 hours. You can still update your event whenever you wish.";
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "EVENT_ID", nullable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false)
	@NotEmpty(message = "Event name must not be empty")
	private String name;
	
	@Column(name = "DESCRIPTION", nullable = true, length = 1000)
	private String description;
	
	@Column(name = "RUN_DATE_FROM", nullable = true)
	private Date runDateFrom;
	
	@Column(name = "RUN_DATE_TO", nullable = true)
	private Date runDateTo;
	
	@Column(name = "TALENT_FEE", nullable = false)
	@NotNull(message = "Please indicate the talent fee for this event")
	private BigDecimal talentFee;
	
	@Column(name = "ACTUAL_TALENT_FEE", nullable = true)
	private BigDecimal actualTalentFee;
	
	@ManyToOne
	@JoinColumn(name="AGENCY_ID")
	private Agency agency;
	
	@OneToMany(mappedBy = "event")
	private List<TalentEvent> talentEvents;
	
	@Column(name = "EVENT_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventStatus status;
	
	@Column(name = "ADMIN_NOTE", nullable = true)
	private String adminNote;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false)
	private Date dateCreated;
	
	public Event() { }
	
	@PrePersist
	public void prePersist() {
		dateCreated = new Date();
		status = EventStatus.FOR_APPROVAL;
		setAdminNote(FOR_APPROVAL_NOTE);
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

	public BigDecimal getActualTalentFee() {
		return actualTalentFee;
	}

	public void setActualTalentFee(BigDecimal actualTalentFee) {
		this.actualTalentFee = actualTalentFee;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public List<TalentEvent> getTalentEvents() {
		return talentEvents;
	}

	public void setTalentEvents(List<TalentEvent> talentEvents) {
		this.talentEvents = talentEvents;
	}

	public EventStatus getStatus() {
		return status;
	}

	public void setStatus(EventStatus status) {
		this.status = status;
	}

	public String getAdminNote() {
		return adminNote;
	}

	public void setAdminNote(String adminNote) {
		this.adminNote = adminNote;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}