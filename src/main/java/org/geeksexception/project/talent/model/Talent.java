package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.enums.TalentStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
	@NotNull(message = "birth date should not be empty")
	private Date birthDate;
	
	@Column(name = "FEET", nullable = true)
	private Integer feet;
	
	@Column(name = "INCHES", nullable = true)
	private Integer inches;
	
	@Column(name = "WEIGHT", nullable = true)
	private BigDecimal weight;
	
	@Column(name = "CONTACT", nullable = false)
	@NotEmpty(message = "Contact no. must not be empty")
	private String contactNo;
	
	@Column(name = "CITY", nullable = false)
	@NotEmpty(message = "City must not be empty")
	private String city;
	
	@Column(name = "TALENT_CLASS", nullable = true)
	@Enumerated(EnumType.STRING)
	private TalentClass talentClass;
	
	@Column(name = "VITAL1", nullable = true)
	private Integer vital1;
	
	@Column(name = "VITAL2", nullable = true)
	private Integer vital2;
	
	@Column(name = "VITAL3", nullable = true)
	private Integer vital3;
	
	@ElementCollection
	@Column(name = "FILE_LOCATION")
	@Fetch(FetchMode.JOIN)
	@NotNull(message = "Please upload at least 1 picture")
	private List<String> imageFileNames;
	
	@OneToOne(mappedBy = "talent")
	@Fetch(FetchMode.JOIN)
	private @Valid User user;
	
	@Column(name = "TALENT_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private TalentStatus status;
	
	public Talent() { }
	
	@PrePersist
	public void prePersist() {
		status = TalentStatus.FOR_APPROVAL;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getFeet() {
		return feet;
	}

	public void setFeet(Integer feet) {
		this.feet = feet;
	}

	public Integer getInches() {
		return inches;
	}

	public void setInches(Integer inches) {
		this.inches = inches;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public TalentClass getTalentClass() {
		return talentClass;
	}

	public void setTalentClass(TalentClass talentClass) {
		this.talentClass = talentClass;
	}

	public Integer getVital1() {
		return vital1;
	}

	public void setVital1(Integer vital1) {
		this.vital1 = vital1;
	}

	public Integer getVital2() {
		return vital2;
	}

	public void setVital2(Integer vital2) {
		this.vital2 = vital2;
	}

	public Integer getVital3() {
		return vital3;
	}

	public void setVital3(Integer vital3) {
		this.vital3 = vital3;
	}

	public List<String> getImageFileNames() {
		return imageFileNames;
	}

	public void setImageFileNames(List<String> imageFileNames) {
		this.imageFileNames = imageFileNames;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public TalentStatus getStatus() {
		return status;
	}

	public void setStatus(TalentStatus status) {
		this.status = status;
	}
	
}