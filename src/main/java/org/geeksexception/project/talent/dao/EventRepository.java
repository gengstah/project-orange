package org.geeksexception.project.talent.dao;

import org.geeksexception.project.talent.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
	
}