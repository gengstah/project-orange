package org.geeksexception.project.talent.dao;

import java.util.List;

import org.geeksexception.project.talent.model.Agency;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
	
	@Query("SELECT a FROM Agency a WHERE a.status = 'APPROVED' ORDER BY a.user.dateCreated DESC")
	List<Agency> findApprovedAgencies(Pageable pageable);
	
	@Query("SELECT COUNT(a) FROM Agency a WHERE a.status = 'APPROVED'")
	Integer countApprovedAgencies();
	
	@Query("SELECT a FROM Agency a WHERE a.status = 'FOR_APPROVAL' ORDER BY a.user.dateCreated DESC")
	List<Agency> findForApprovalAgencies(Pageable pageable);
	
	@Query("SELECT COUNT(a) FROM Agency a WHERE a.status = 'FOR_APPROVAL'")
	Integer countForApprovalAgencies();
	
	@Query("SELECT a FROM Agency a WHERE a.status = 'DENIED' ORDER BY a.user.dateCreated DESC")
	List<Agency> findDeniedAgencies(Pageable pageable);
	
	@Query("SELECT COUNT(a) FROM Agency a WHERE a.status = 'DENIED'")
	Integer countDeniedAgencies();
	
	@Query("SELECT COUNT(e) FROM Agency a JOIN a.events e WHERE a = ?1")
	Integer countEvents(Agency agency);
	
}