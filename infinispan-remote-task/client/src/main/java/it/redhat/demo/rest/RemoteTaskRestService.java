/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.rest;

import java.util.Collections;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.infinispan.client.hotrod.RemoteCache;

import org.slf4j.Logger;

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
	private Logger log;

	// remote task invocation needs default marshaller
	// see https://issues.jboss.org/browse/ISPN-8020
	@Inject
	@JBossMarshalling
	private RemoteCache<String, Project> cache;

	@Inject
	@JBossMarshalling
	private ProjectService projectService;

	@GET
	@Path( "ciao" )
	public Object ciaoRemoteTask() {

		return cache.execute( "CiaoRemoteTask", Collections.emptyMap() );

	}

	@POST
	@Path( "init" )
	public void init() {

		cache.execute( "AddProtobufTask", Collections.emptyMap() );

	}

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
