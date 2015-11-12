package org.geeksexception.project.talent.api;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geeksexception.project.talent.dao.specification.criteria.EventCriteria;
import org.geeksexception.project.talent.enums.UserRole;
import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Error;
import org.geeksexception.project.talent.model.Errors;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.model.TalentEvent;
import org.geeksexception.project.talent.model.User;
import org.geeksexception.project.talent.service.EventService;
import org.geeksexception.project.talent.service.TalentEventService;
import org.geeksexception.project.talent.service.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;

@Path("/event")
public class EventManager {
	
	private @Inject EventService eventService;
	
	private @Inject TalentEventService talentEventService;
	
	private @Inject UserService userService;
	
	public EventManager() { }
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public List<Event> findAllEvents(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.findAllEvents(page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approved")
	public List<Event> searchApprovedEvents(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.searchApprovedEvents(eventCriteria, page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/forApproval")
	public List<Event> searchForApprovalEvents(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.searchForApprovalEvents(eventCriteria, page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/denied")
	public List<Event> searchDeniedEvents(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.searchDeniedEvents(eventCriteria, page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/closed")
	public List<Event> searchClosedEvents(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.searchClosedEvents(eventCriteria, page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	public Event save(Event event) throws TalentManagementServiceApiException {
		
		return eventService.save(event);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/{id}")
	public Event findEvent(@NotNull(message = "id must not be null") @PathParam("id") Long id) {
		
		return eventService.findEvent(id);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/agency/approved")
	public List<Event> searchApprovedEventsOfAgency(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) throws TalentManagementServiceApiException {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		setAgencyId(eventCriteria);
		
		return eventService.searchApprovedEvents(eventCriteria, page, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/agency/forApproval")
	public List<Event> searchForApprovalEventsOfAgency(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) throws TalentManagementServiceApiException {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		setAgencyId(eventCriteria);
		
		return eventService.searchForApprovalEvents(eventCriteria, page, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/agency/denied")
	public List<Event> searchDeniedEventsOfAgency(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) throws TalentManagementServiceApiException {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		setAgencyId(eventCriteria);
		
		return eventService.searchDeniedEvents(eventCriteria, page, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/agency/closed")
	public List<Event> searchClosedEventsOfAgency(
			EventCriteria eventCriteria,
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) throws TalentManagementServiceApiException {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		setAgencyId(eventCriteria);
		
		return eventService.searchClosedEvents(eventCriteria, page, size);
		
	}

	private void setAgencyId(EventCriteria eventCriteria) throws TalentManagementServiceApiException {
		User user = userService.getLoggedInUser();
		if(user == null)
			throw new TalentManagementServiceApiException(
					"User not logged in", 
					new Errors()
						.addError(new Error("user", "User not logged in")));
		
		if(user.getUserRole() == UserRole.ROLE_USER)
			throw new TalentManagementServiceApiException(
					"User is not allowed to make this request", 
					new Errors()
						.addError(new Error("user", "User is not allowed to make this request")));
		
		eventCriteria.setAgencyId(user.getAgency().getId());
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/talent/{id}")
	public List<Event> findAllEventsOfTalent(
			@NotNull(message = "id must not be null") @PathParam("id") Long id, 
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.findAllEventsOfTalent(id, page - 1, size);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approve")
	public void approve(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotNull(message = "actual talent fee must not be empty") @FormParam("actualTalentFee") BigDecimal actualTalentFee) throws TalentManagementServiceApiException {
		
		eventService.approveEvent(id, actualTalentFee);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/deny")
	public void deny(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "admin note must not be empty") @FormParam("adminNote") String adminNote) throws TalentManagementServiceApiException {
		
		eventService.denyEvent(id, adminNote);
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/setForApproval")
	public void forApproval(@NotNull(message = "id must not be null") @FormParam("id") Long id, 
			@NotEmpty(message = "admin note must not be empty") @FormParam("adminNote") String adminNote) throws TalentManagementServiceApiException {
		
		eventService.forApprovalEvent(id, adminNote);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/talentEvent/event/{eventId}")
	public List<TalentEvent> findAllTalentEventByEventId(@NotNull(message = "eventId must not be null") @PathParam("eventId") Long eventId) {
		
		return talentEventService.findAllTalentEventByEventId(eventId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/talentEvent/talent/{eventId}")
	public List<TalentEvent> findAllTalentEventByTalentId(@NotNull(message = "talentId must not be null") @PathParam("talentId") Long talentId) {
		
		return talentEventService.findAllTalentEventByTalentId(talentId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countApprovedEvents")
	public Long countApprovedEvents() {
		
		return eventService.countApprovedEvents();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countForApprovalEvents")
	public Long countForApprovalEvents() {
		
		return eventService.countForApprovalEvents();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countDeniedEvents")
	public Long countDeniedEvents() {
		
		return eventService.countDeniedEvents();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countClosedEvents")
	public Long countClosedEvents() {
		
		return eventService.countClosedEvents();
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countApprovedEventsByAgency/{agencyId}")
	public Long countApprovedEventsByAgency(@NotNull(message = "agencyId must not be null") @PathParam("agencyId") Long agencyId) {
		
		return eventService.countApprovedEventsByAgency(agencyId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countForApprovalEventsByAgency/{agencyId}")
	public Long countForApprovalEventsByAgency(@NotNull(message = "agencyId must not be null") @PathParam("agencyId") Long agencyId) {
		
		return eventService.countForApprovalEventsByAgency(agencyId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countDeniedEventsByAgency/{agencyId}")
	public Long countDeniedEventsByAgency(@NotNull(message = "agencyId must not be null") @PathParam("agencyId") Long agencyId) {
		
		return eventService.countDeniedEventsByAgency(agencyId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/countClosedEventsByAgency/{agencyId}")
	public Long countClosedEventsByAgency(@NotNull(message = "agencyId must not be null") @PathParam("agencyId") Long agencyId) {
		
		return eventService.countClosedEventsByAgency(agencyId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/findApprovedEventsOfAgencyNotAppliedByTalent/{agencyId}/{talentId}")
	public List<Event> findApprovedEventsOfAgencyNotAppliedByTalent(
			@NotNull(message = "agencyId must not be null") @PathParam("agencyId") Long agencyId, 
			@NotNull(message = "talentId must not be null") @PathParam("talentId") Long talentId) {
		
		return eventService.findApprovedEventsOfAgencyNotAppliedByTalent(agencyId, talentId);
		
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/findApprovedEventsOfAgencyAppliedByTalent/{agencyId}/{talentId}")
	public List<Event> findApprovedEventsOfAgencyAppliedByTalent(
			@NotNull(message = "agencyId must not be null") @PathParam("agencyId") Long agencyId, 
			@NotNull(message = "talentId must not be null") @PathParam("talentId") Long talentId) {
		
		return eventService.findApprovedEventsOfAgencyAppliedByTalent(agencyId, talentId);
		
	}
	
}