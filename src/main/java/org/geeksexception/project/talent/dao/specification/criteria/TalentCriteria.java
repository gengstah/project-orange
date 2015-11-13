package org.geeksexception.project.talent.dao.specification.criteria;

import org.geeksexception.project.talent.enums.Gender;
import org.geeksexception.project.talent.enums.TalentClass;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TalentCriteria {
	
	private String firstName;
	
	private String lastName;
	
	private Gender gender;
	
	private TalentClass talentClass;
	
	private Integer ageFrom;
	
	private Integer ageTo;
	
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

	public TalentClass getTalentClass() {
		return talentClass;
	}

	@JsonProperty
	public void setTalentClass(TalentClass talentClass) {
		this.talentClass = talentClass;
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

	public String getCity() {
		return city;
	}

	@JsonProperty
	public void setCity(String city) {
		this.city = city;
	}
	
}