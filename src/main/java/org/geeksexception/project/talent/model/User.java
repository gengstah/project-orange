package org.geeksexception.project.talent.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.geeksexception.project.talent.enums.UserRole;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "APP_USER")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = User.class)
public class User implements Serializable {
	
	private static final long serialVersionUID = 4835484455062843293L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "USER_ID", nullable = false)
	private Long id;
	
	@Column(name = "EMAIL", nullable = false)
	@NotEmpty(message = "Email must not be empty")
	@Email(regexp = "[A-Za-z0-9._%+-]+@(?:[A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}", message = "Email is invalid")
	private String email;
	
	@NotEmpty(message = "password must not be empty")
	@Size(min = 8, message = "password must consist of at least 8 characters")
	private String password;
	
	@Column(name = "USER_ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	
	@OneToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="AGENCY_ID")
	private Agency agency;
	
	@OneToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="TALENT_ID")
	private Talent talent;
	
	public User() { }
	
}