package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.geeksexception.project.talent.enums.AgencyStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "AGENCY")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = Agency.class)
public class Agency implements Serializable {
	
	private static final long serialVersionUID = 379016750642204578L;
	
	private static final String FOR_APPROVAL_NOTE = "Your account is still for approval. Please allow us to process your registration within 24-48 hours. You can still update your profile whenever you wish.";
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "AGENCY_ID", nullable = false)
	private Long id;
	
	@Column(name = "FIRSTNAME", nullable = false)
	@NotEmpty(message = "firstName must not be empty")
	private String firstName;

	@Column(name = "LASTNAME", nullable = false)
	@NotEmpty(message = "lastName must not be empty")
	private String lastName;
	
	@Column(name = "CONTACT", nullable = false)
	@NotEmpty(message = "Contact no. must not be empty")
	private String contactNo;
	
	@Column(name = "AGENCY_NAME", nullable = false)
	@NotEmpty(message = "Agency name must not be empty")
	private String agencyName;
	
	@Column(name = "AGENCY_ADDRESS", nullable = true)
	private String agencyAddress;
	
	@OneToOne(mappedBy = "agency")
	@Fetch(FetchMode.JOIN)
	private User user;
	
	@OneToMany(mappedBy = "agency")
	private List<Event> events;
	
	@Transient
	private Integer eventSize;
	
	@Column(name = "AGENCY_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private AgencyStatus status;
	
	@Column(name = "ADMIN_NOTE", nullable = true)
	private String adminNote;
	
	public Agency() { }
	
	@PrePersist
	public void prePersist() {
		status = AgencyStatus.FOR_APPROVAL;
		adminNote = FOR_APPROVAL_NOTE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAgencyName() {
		return agencyName;
	}

	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}

	public String getAgencyAddress() {
		return agencyAddress;
	}

	public void setAgencyAddress(String agencyAddress) {
		this.agencyAddress = agencyAddress;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	@JsonProperty
	public Integer getEventSize() {
		return eventSize;
	}

	public void setEventSize(Integer eventSize) {
		this.eventSize = eventSize;
	}

	public AgencyStatus getStatus() {
		return status;
	}

	public void setStatus(AgencyStatus status) {
		this.status = status;
	}

	public String getAdminNote() {
		return adminNote;
	}

	public void setAdminNote(String adminNote) {
		this.adminNote = adminNote;
	}
	
}