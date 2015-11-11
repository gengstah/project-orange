package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	@Query("SELECT e FROM Event e ORDER BY e.dateCreated DESC")
	List<Event> findAllEvents(Pageable pageable);
	
	@Query("SELECT e FROM Event e WHERE e.status = 'APPROVED' ORDER BY e.dateCreated DESC")
	List<Event> findAllApprovedEvents(Pageable pageable);
	
	@Query("SELECT e FROM Event e WHERE e.agency.id = ?1 ORDER BY e.dateCreated DESC")
	List<Event> findAllEventsOfAgency(Long id, Pageable pageable);
	
	@Query("SELECT e FROM Event e JOIN e.talentEvents te WHERE te.talent.id = ?1 ORDER BY e.dateCreated DESC")
	List<Event> findAllEventsOfTalent(Long id, Pageable pageable);
	
}