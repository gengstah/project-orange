package org.geeksexception.project.talent.dao;

import org.geeksexception.project.talent.model.TalentEvent;
import org.geeksexception.project.talent.model.TalentEventId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TalentEventRepository extends JpaRepository<TalentEvent, TalentEventId> {
	
	@Query("SELECT te FROM TalentEvent te WHERE te.talent.id = ?1 AND te.event.id = ?2")
	TalentEvent findTalentEventByTalentAndEvent(Long talentId, Long eventId);
	
}