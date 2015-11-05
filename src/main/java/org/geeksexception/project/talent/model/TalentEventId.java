package org.geeksexception.project.talent.model;

import java.io.Serializable;

public class TalentEventId implements Serializable {
	
	private static final long serialVersionUID = -3004373307281956680L;

	private Long talent;
	
	private Long event;
	
	public TalentEventId() { }

	public Long getTalent() {
		return talent;
	}

	public void setTalent(Long talent) {
		this.talent = talent;
	}

	public Long getEvent() {
		return event;
	}

	public void setEvent(Long event) {
		this.event = event;
	}
	
}