package org.geeksexception.project.talent.api;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geeksexception.project.talent.exception.TalentManagementServiceApiException;
import org.geeksexception.project.talent.model.Event;
import org.geeksexception.project.talent.service.EventService;
import org.springframework.http.MediaType;

@Path("/event")
public class EventManager {
	
	private @Inject EventService eventService;
	
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
	
}