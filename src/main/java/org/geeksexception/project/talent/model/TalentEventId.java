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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((talent == null) ? 0 : talent.hashCode());
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
		TalentEventId other = (TalentEventId) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (talent == null) {
			if (other.talent != null)
				return false;
		} else if (!talent.equals(other.talent))
			return false;
		return true;
	}
	
}