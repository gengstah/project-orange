package org.geeksexception.project.talent.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.geeksexception.project.talent.dao.TalentRepository;
import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.model.Talent;
import org.geeksexception.project.talent.service.TalentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TalentServiceImpl implements TalentService {
	
	private @Inject TalentRepository talentRepository;
	
	public TalentServiceImpl() { }
	
	@Override
	public List<Talent> findApprovedTalents(Integer page, Integer size) {
		
		return talentRepository.findApprovedTalents(new PageRequest(page, size));
		
	}

	@Override
	public List<Talent> findForApprovalTalents(Integer page, Integer size) {
		
		return talentRepository.findForApprovalTalents(new PageRequest(page, size));
		
	}

	@Override
	public List<Talent> findApprovedTalentsByClass(TalentClass talentClass, Integer page, Integer size) {
		
		return talentRepository.findApprovedTalentsByClass(talentClass, new PageRequest(page, size));
		
	}

	@Override
	public Talent save(Talent talent) {
		
		return talentRepository.save(talent);
		
	}

}