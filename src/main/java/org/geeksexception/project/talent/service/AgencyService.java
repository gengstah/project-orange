package org.geeksexception.project.talent.service;

import java.util.List;

import org.geeksexception.project.talent.model.Agency;

public interface AgencyService {
	
	Agency save(Agency agency);
	
	List<Agency> findApprovedAgencies(Integer page, Integer size);
	
	List<Agency> findForApprovalAgencies(Integer page, Integer size);
	
	List<Agency> findDeniedAgencies(Integer page, Integer size);
	
	void approveAgency(Long id);
	
	void denyAgency(Long id, String adminNote);
	
	void forApprovalAgency(Long id, String adminNote);
	
	Integer countApprovedAgencies();
	
	Integer countForApprovalAgencies();
	
	Integer countDeniedAgencies();
	
}