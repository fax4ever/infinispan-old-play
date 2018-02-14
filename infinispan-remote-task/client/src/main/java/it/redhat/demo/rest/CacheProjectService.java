package it.redhat.demo.rest;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import it.redhat.demo.cache.ProtoStream;
import it.redhat.demo.model.Project;
import it.redhat.demo.query.QueryService;
import it.redhat.demo.service.ProjectService;

/**
 * @author Fabio Massimo Ercoli
 */
@Path( "cache" )
public class CacheProjectService {

	@Inject
	@ProtoStream
	private ProjectService service;

	@Inject
	private QueryService query;

	@GET
	@Path( "project/{projectName}" )
	@Produces( "application/json" )
	public Project getProject( @PathParam( "projectName" ) String projectName ) {

		return service.take( projectName );

	}

	@POST
	@Path( "project/{projectName}" )
	@Produces( "application/json" )
	public Project insertProject( @PathParam( "projectName" ) String projectName ) {

		return service.create( projectName );

	}

	@PUT
	@Path( "project/{projectName}" )
	@Produces( "application/json" )
	public Project incrementProjectCode( @PathParam( "projectName" ) String projectName) {

		return service.update( projectName );

	}

	@GET
	@Path( "project/code/{code}" )
	@Produces( "application/json" )
	public List<Project> findAllProcessByCode( @PathParam( "code" ) Integer code ) {

		return query.findByCode( code );

	}

}
