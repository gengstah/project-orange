package org.geeksexception.project.talent.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "TALENT_IMAGE")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@identity", scope = Image.class)
public class Image implements Serializable {
	
	private static final long serialVersionUID = 4423958681415358043L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name = "TALENT_IMAGE_ID", nullable = false)
	private Long id;
	
	@Column(name = "FILE_LOCATION", nullable = false)
	@NotEmpty(message = "File must not be empty")
	private String fileLocation;
	
	@ManyToOne
	@JoinColumn(name="TALENT_ID")
	private Talent talent;
	
	public Image() { }
	
	public Image(String fileLocation) { this.fileLocation = fileLocation; }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	
}