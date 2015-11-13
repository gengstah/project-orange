package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.enums.EventStatus;
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
	
	@Query("SELECT e FROM Event e WHERE e.agency.id = ?1 AND e.status = 'APPROVED' ORDER BY e.dateCreated DESC")
	List<Event> findAllApprovedEventsOfAgency(Long id, Pageable pageable);
	
	@Query("SELECT e FROM Event e JOIN e.talentEvents te WHERE te.talent.id = ?1 ORDER BY e.dateCreated DESC")
	List<Event> findAllEventsOfTalent(Long id, Pageable pageable);
	
	@Query("SELECT COUNT(e) FROM Event e WHERE e.status = ?1")
	Integer countEventsByStatus(EventStatus eventStatus);
	
	@Query("SELECT COUNT(e) FROM Event e WHERE e.status = ?1 AND e.agency.id = ?2")
	Integer countEventsByStatusAndAgency(EventStatus eventStatus, Long agencyId);
	
	@Query("SELECT e FROM Event e WHERE e.status = 'APPROVED' and e.agency.id = ?1 AND e NOT IN (SELECT ev FROM Event ev JOIN ev.talentEvents te WHERE ev.status = 'APPROVED' AND te.event.agency.id = ?1 AND te.talent.id = ?2)")
	List<Event> findApprovedEventsOfAgencyNotAppliedByTalent(Long agencyId, Long talentId);
	
	@Query("SELECT e FROM Event e JOIN e.talentEvents te WHERE te.event.agency.id = ?1 AND te.talent.id = ?2 AND te.event.status = 'APPROVED'")
	List<Event> findApprovedEventsOfAgencyAppliedByTalent(Long agencyId, Long talentId);
	
}