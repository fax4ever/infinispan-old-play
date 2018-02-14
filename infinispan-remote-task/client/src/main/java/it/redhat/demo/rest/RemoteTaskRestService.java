package it.redhat.demo.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import it.redhat.demo.cache.JBossMarshalling;
import it.redhat.demo.model.Project;
import it.redhat.demo.service.ProjectService;

/**
 * @author Fabio Massimo Ercoli
 */
@Stateless
@Path( "task" )
public class RemoteTaskRestService {

	@Inject
	@JBossMarshalling
	private ProjectService projectService;

	@GET
	@Path( "project/{projectName}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Project getProjectByNameTask( @PathParam( "projectName" ) String projectName ) {

		return projectService.take( projectName );

	}

	@POST
	@Path( "project/{projectName}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Project createProjectByNameTask( @PathParam( "projectName" ) String projectName ) {

		return projectService.create( projectName );

	}

	@PUT
	@Path( "project/{projectName}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Project incrementProjectCode( @PathParam( "projectName" ) String projectName ) {

		return projectService.update( projectName );

	}

}
