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
@Table(name = "WORK_EXPERIENCE")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = WorkExperience.class)
public class WorkExperience implements Serializable {
	
	private static final long serialVersionUID = -132367463539106327L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "WORK_EXPERIENCE_ID", nullable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false)
	@NotEmpty(message = "Work experience name must not be empty")
	private String name;
	
	public WorkExperience() { }

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
	
}