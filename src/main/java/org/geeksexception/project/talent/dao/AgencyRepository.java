package org.geeksexception.project.talent.dao;

import org.geeksexception.project.talent.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
	
}