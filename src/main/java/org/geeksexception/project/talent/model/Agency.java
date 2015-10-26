package org.geeksexception.project.talent.model;

import java.io.Serializable;

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
@Table(name = "AGENCY")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = Agency.class)
public class Agency implements Serializable {
	
	private static final long serialVersionUID = 379016750642204578L;
	
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
	@NotEmpty(message = "lastName must not be empty")
	private String agencyName;
	
	@Column(name = "AGENCY_ADDRESS", nullable = true)
	private String agencyAddress;
	
	public Agency() { }
	
}