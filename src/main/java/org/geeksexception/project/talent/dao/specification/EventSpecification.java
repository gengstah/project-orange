package org.geeksexception.project.talent.dao.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.apache.commons.lang.StringUtils.*;

import org.geeksexception.project.talent.dao.specification.criteria.EventCriteria;
import org.geeksexception.project.talent.enums.EventStatus;
import org.geeksexception.project.talent.model.Agency;
import org.geeksexception.project.talent.model.Event;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {
	
	public static Specification<Event> eventsMatchingSearchCriteria(final EventCriteria eventCriteria, final EventStatus eventStatus) {
		
		return new Specification<Event>() {

			@Override
			public Predicate toPredicate(Root<Event> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> criteria = new ArrayList<Predicate>();
				if(isNotEmpty(eventCriteria.getName())) {
					criteria.add(cb.like(cb.upper(root.<String>get("name")), "%" + eventCriteria.getName().toUpperCase() + "%"));
				}
				
				if(eventCriteria.getRunDate() != null) {
					criteria.add(cb.greaterThanOrEqualTo(root.<Date>get("runDateFrom"), eventCriteria.getRunDate()));
					criteria.add(cb.lessThanOrEqualTo(root.<Date>get("runDateTo"), eventCriteria.getRunDate()));
				}
				
				if(eventCriteria.getTalentFeeFrom() != null) {
					criteria.add(cb.greaterThanOrEqualTo(root.<BigDecimal>get("talentFee"), eventCriteria.getTalentFeeFrom()));
				}
				
				if(eventCriteria.getTalentFeeTo() != null) {
					criteria.add(cb.lessThanOrEqualTo(root.<BigDecimal>get("talentFee"), eventCriteria.getTalentFeeTo()));
				}
				
				if(eventCriteria.getDateCreatedFrom() != null) {
					criteria.add(cb.greaterThanOrEqualTo(root.<Date>get("dateCreated"), eventCriteria.getDateCreatedFrom()));
				}
				
				if(eventCriteria.getDateCreatedTo() != null) {
					criteria.add(cb.lessThanOrEqualTo(root.<Date>get("dateCreated"), eventCriteria.getDateCreatedTo()));
				}
				
				if(eventCriteria.getAgencyId() != null) {
					criteria.add(cb.equal(root.<Agency>get("agency").<Long>get("id"), eventCriteria.getAgencyId()));
				}
				
				criteria.add(cb.equal(root.<EventStatus>get("status"), eventStatus));
				
				return cb.and(criteria.toArray(new Predicate[0]));
				
			}
			
		};
		
	}
	
}