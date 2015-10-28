package org.geeksexception.project.talent.enums;

public enum Gender {
	M("Male"), F("Female");
	
	private String description;
	
	private Gender(String description) {
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}