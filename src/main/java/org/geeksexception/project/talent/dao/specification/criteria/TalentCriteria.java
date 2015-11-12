package org.geeksexception.project.talent.dao.specification.criteria;

import java.math.BigDecimal;
import java.util.List;

import org.geeksexception.project.talent.enums.Gender;
import org.geeksexception.project.talent.enums.TalentClass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TalentCriteria {
	
	private String firstName;
	
	private String lastName;
	
	private Gender gender;
	
	private List<TalentClass> talentClasses;
	
	private Integer ageFrom;
	
	private Integer ageTo;
	
	private BigDecimal talentFeeFrom;
	
	private BigDecimal talentFeeTo;
	
	private String city;
	
	public TalentCriteria() { }

	public String getFirstName() {
		return firstName;
	}

	@JsonProperty
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@JsonProperty
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Gender getGender() {
		return gender;
	}

	@JsonProperty
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<TalentClass> getTalentClasses() {
		return talentClasses;
	}

	@JsonProperty
	public void setTalentClasses(List<TalentClass> talentClasses) {
		this.talentClasses = talentClasses;
	}

	public Integer getAgeFrom() {
		return ageFrom;
	}

	@JsonProperty
	public void setAgeFrom(Integer ageFrom) {
		this.ageFrom = ageFrom;
	}

	public Integer getAgeTo() {
		return ageTo;
	}

	@JsonProperty
	public void setAgeTo(Integer ageTo) {
		this.ageTo = ageTo;
	}

	public BigDecimal getTalentFeeFrom() {
		return talentFeeFrom;
	}

	public void setTalentFeeFrom(BigDecimal talentFeeFrom) {
		this.talentFeeFrom = talentFeeFrom;
	}

	public BigDecimal getTalentFeeTo() {
		return talentFeeTo;
	}

	public void setTalentFeeTo(BigDecimal talentFeeTo) {
		this.talentFeeTo = talentFeeTo;
	}

	public String getCity() {
		return city;
	}

	@JsonProperty
	public void setCity(String city) {
		this.city = city;
	}
	
}