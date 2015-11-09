package org.geeksexception.project.talent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.AgencyRepository;
import org.geeksexception.project.talent.enums.AgencyStatus;
import org.geeksexception.project.talent.model.Agency;
import org.geeksexception.project.talent.service.AgencyService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgencyServiceImpl implements AgencyService {
	
	private @Inject AgencyRepository agencyRepository;
	
	public AgencyServiceImpl() { }

	@Override
	@Transactional(readOnly = false)
	public Agency save(Agency agency) {
		
		return agencyRepository.save(agency);
		
	}

	@Override
	public List<Agency> findApprovedAgencies(Integer page, Integer size) {
		
		return agencyRepository.findApprovedAgencies(new PageRequest(page, size));
		
	}

	@Override
	public List<Agency> findForApprovalAgencies(Integer page, Integer size) {
		
		return agencyRepository.findForApprovalAgencies(new PageRequest(page, size));
		
	}

	@Override
	public List<Agency> findDeniedAgencies(Integer page, Integer size) {
		
		return agencyRepository.findDeniedAgencies(new PageRequest(page, size));
		
	}

	@Override
	@Transactional(readOnly = false)
	public void approveAgency(Long id) {
		
		Agency agency = agencyRepository.findOne(id);
		agency.setStatus(AgencyStatus.APPROVED);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void denyAgency(Long id, String adminNote) {
		
		Agency agency = agencyRepository.findOne(id);
		agency.setStatus(AgencyStatus.DENIED);
		agency.setAdminNote(adminNote);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void forApprovalAgency(Long id, String adminNote) {
		
		Agency agency = agencyRepository.findOne(id);
		agency.setStatus(AgencyStatus.FOR_APPROVAL);
		agency.setAdminNote(adminNote);
		
	}

	@Override
	public Integer countApprovedAgencies() {
		
		return agencyRepository.countApprovedAgencies();
		
	}

	@Override
	public Integer countForApprovalAgencies() {
		
		return agencyRepository.countForApprovalAgencies();
		
	}

	@Override
	public Integer countDeniedAgencies() {
		
		return agencyRepository.countDeniedAgencies();
		
	}

}