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

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.model.TalentEvent;
import org.geeksexception.project.talent.service.EventService;
import org.geeksexception.project.talent.service.TalentEventService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;

@Path("/event")
public class EventManager {
	
	private @Inject EventService eventService;
	
	private @Inject TalentEventService talentEventService;
	
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
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/approved")
	public List<Event> findAllApprovedEvents(
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.findAllApprovedEvents(page - 1, size);
		
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
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.APPLICATION_JSON_VALUE)
	@Path("/agency/{id}")
	public List<Event> findAllEventsOfAgency(
			@NotNull(message = "id must not be null") @PathParam("id") Long id, 
			@QueryParam("page") @Min(1) Integer page, 
			@QueryParam("size") @Min(1) Integer size) {
		
		if(page == null) page = 1;
		if(size == null) size = 20;
		
		return eventService.findAllEventsOfAgency(id, page - 1, size);
		
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
	
}