package org.geeksexception.project.talent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.geeksexception.project.talent.enums.UserRole;
import org.geeksexception.project.talent.enums.UserStatus;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	
	@Column(name = "PASSWORD", nullable = false)
	@NotEmpty(message = "password must not be empty")
	@Size(min = 8, message = "password must consist of at least 8 characters")
	private String password;
	
	@Column(name = "USER_ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	
	@Column(name = "USER_STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
	
	@OneToOne
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="AGENCY_ID")
	private @Valid Agency agency;
	
	@OneToOne(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name="TALENT_ID")
	private @Valid Talent talent;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_CREATED", nullable = false)
	private Date dateCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_LAST_LOGIN", nullable = true)
	private Date dateLastLogin;
	
	@ManyToMany
	@JoinTable(name = "USER_FOLLOWERS", 
		joinColumns = {@JoinColumn(name="USER_ID")},
		inverseJoinColumns = {@JoinColumn(name="FOLLOWER_USER_ID")})
	private List<User> followers;
	
	@Transient
	private Integer followerSize;
	
	@ManyToMany(mappedBy = "followers")
	private List<User> following;
	
	@Transient
	private Integer followingSize;
	
	@Transient
	private String reCaptchaResponse;
	
	public User() { }
	
	@PrePersist
	public void prePersist() {
		dateCreated = new Date();
		userStatus = UserStatus.ACTIVE;
		userRole = UserRole.ROLE_USER;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public Talent getTalent() {
		return talent;
	}

	public void setTalent(Talent talent) {
		this.talent = talent;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastLogin() {
		return dateLastLogin;
	}

	public void setDateLastLogin(Date dateLastLogin) {
		this.dateLastLogin = dateLastLogin;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	@JsonProperty
	public Integer getFollowerSize() {
		return followerSize;
	}

	public void setFollowerSize(Integer followerSize) {
		this.followerSize = followerSize;
	}

	public List<User> getFollowing() {
		return following;
	}

	public void setFollowing(List<User> following) {
		this.following = following;
	}

	@JsonProperty
	public Integer getFollowingSize() {
		return followingSize;
	}

	public void setFollowingSize(Integer followingSize) {
		this.followingSize = followingSize;
	}

	public String getReCaptchaResponse() {
		return reCaptchaResponse;
	}
	
	@JsonProperty
	public void setReCaptchaResponse(String reCaptchaResponse) {
		this.reCaptchaResponse = reCaptchaResponse;
	}
	
}