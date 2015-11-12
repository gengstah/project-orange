package org.geeksexception.project.talent.dao.specification.criteria;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventCriteria {
	
	private String name;
	
	private Date runDate;
	
	private BigDecimal talentFeeFrom;
	
	private BigDecimal talentFeeTo;
	
	private Date dateCreatedFrom;
	
	private Date dateCreatedTo;
	
	public EventCriteria() { }

	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}

	public Date getRunDate() {
		return runDate;
	}

	@JsonProperty
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public BigDecimal getTalentFeeFrom() {
		return talentFeeFrom;
	}

	@JsonProperty
	public void setTalentFeeFrom(BigDecimal talentFeeFrom) {
		this.talentFeeFrom = talentFeeFrom;
	}

	public BigDecimal getTalentFeeTo() {
		return talentFeeTo;
	}

	@JsonProperty
	public void setTalentFeeTo(BigDecimal talentFeeTo) {
		this.talentFeeTo = talentFeeTo;
	}

	public Date getDateCreatedFrom() {
		return dateCreatedFrom;
	}

	@JsonProperty
	public void setDateCreatedFrom(Date dateCreatedFrom) {
		this.dateCreatedFrom = dateCreatedFrom;
	}

	public Date getDateCreatedTo() {
		return dateCreatedTo;
	}

	@JsonProperty
	public void setDateCreatedTo(Date dateCreatedTo) {
		this.dateCreatedTo = dateCreatedTo;
	}
	
}