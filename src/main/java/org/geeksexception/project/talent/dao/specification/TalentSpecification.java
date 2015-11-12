package org.geeksexception.project.talent.dao.specification;

import static org.apache.commons.lang.StringUtils.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.geeksexception.project.talent.dao.specification.criteria.TalentCriteria;
import org.geeksexception.project.talent.enums.Gender;
import org.geeksexception.project.talent.enums.TalentClass;
import org.geeksexception.project.talent.enums.TalentStatus;
import org.geeksexception.project.talent.model.Talent;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

public class TalentSpecification {
	
	public static Specification<Talent> talentsMatchingSearchCriteria(final TalentCriteria talentCriteria, final TalentStatus talentStatus) {
		
		return new Specification<Talent>() {

			@Override
			public Predicate toPredicate(Root<Talent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> criteria = new ArrayList<Predicate>();
				if(isNotEmpty(talentCriteria.getFirstName())) {
					criteria.add(cb.like(cb.upper(root.<String>get("firstName")), "%" + talentCriteria.getFirstName().toUpperCase() + "%"));
				}
				
				if(isNotEmpty(talentCriteria.getLastName())) {
					criteria.add(cb.like(cb.upper(root.<String>get("lastName")), "%" + talentCriteria.getLastName().toUpperCase() + "%"));
				}
				
				if(talentCriteria.getGender() != null) {
					criteria.add(cb.equal(root.<Gender>get("gender"), talentCriteria.getGender()));
				}
				
				if(talentCriteria.getTalentClasses() != null && talentCriteria.getTalentClasses().size() > 0) {
					List<Predicate> talentClassPredicates = new ArrayList<Predicate>();
					
					for(TalentClass talentClass : talentCriteria.getTalentClasses()) {
						talentClassPredicates.add(cb.equal(root.<TalentClass>get("talentClass"), talentClass));
					}
					
					criteria.add(cb.or(talentClassPredicates.toArray(new Predicate[0])));
				}
				
				if(talentCriteria.getAgeFrom() != null && talentCriteria.getAgeFrom() >= 18) {
					criteria.add(cb.lessThanOrEqualTo(root.<Date>get("birthDate"), new DateTime().minusYears(talentCriteria.getAgeFrom()).toDate()));
				}
				
				if(talentCriteria.getAgeTo() != null && talentCriteria.getAgeTo() >= 18) {
					criteria.add(cb.greaterThanOrEqualTo(root.<Date>get("birthDate"), new DateTime().minusYears(talentCriteria.getAgeTo() + 1).minusDays(1).toDate()));
				}
				
				if(talentCriteria.getTalentFeeFrom() != null) {
					criteria.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("expectedSalary"), talentCriteria.getTalentFeeFrom()));
				}
				
				if(talentCriteria.getTalentFeeTo() != null) {
					criteria.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("expectedSalary"), talentCriteria.getTalentFeeTo()));
				}
				
				if(isNotEmpty(talentCriteria.getCity())) {
					criteria.add(cb.like(cb.upper(root.<String>get("city")), "%" + talentCriteria.getCity().toUpperCase() + "%"));
				}
				
				criteria.add(cb.equal(root.<TalentStatus>get("status"), talentStatus));
				
				return cb.and(criteria.toArray(new Predicate[0]));
			}
			
		};
		
	}
	
}