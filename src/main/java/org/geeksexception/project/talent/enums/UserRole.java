package org.geeksexception.project.talent.enums;

public enum UserRole {
	ROLE_ADMIN("ROLE_ADMIN"), ROLE_AGENCY("ROLE_AGENCY"), ROLE_USER("ROLE_USER");
	
	private String description;
	
	private UserRole(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return description;
	}
	
}