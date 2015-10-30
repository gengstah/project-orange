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
	
	public WorkExperience(String name) { this.name = name; }

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name.toUpperCase() == null) ? 0 : name.toUpperCase().hashCode());
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
		WorkExperience other = (WorkExperience) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.toUpperCase().equals(other.name.toUpperCase()))
			return false;
		return true;
	}
	
}