/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.infinispan.client.hotrod.RemoteCache;

import org.slf4j.Logger;

import it.redhat.demo.cache.ProtoStream;
import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@Stateless
@Path( "cache" )
public class RemoteCacheRestService {

	@Inject
	private Logger log;

	@Inject
	@ProtoStream
	private RemoteCache<String, Project> protoCache;

	@GET
	@Path( "project/{projectName}" )
	@Produces( "application/json" )
	public Project getProject( @PathParam( "projectName" ) String projectName) {

		Project project = protoCache.get( projectName );

		log.info( "Getting new project {} with code 1 {}", projectName, project );

		return project;

	}

	@POST
	@Path( "project/{projectName}" )
	@Produces( "application/json" )
	public Project insertProject( @PathParam( "projectName" ) String projectName) {

		Project project = new Project();
		project.setCode( 1 );
		project.setDescription( projectName );
		project.setName( projectName );

		protoCache.put( projectName, project );
		project = protoCache.get( projectName );

		log.info( "Created new project {} with code 1 {}", projectName, project );

		return project;

	}

	@PUT
	@Path( "project/{projectName}" )
	@Produces( "application/json" )
	public Project incrementProjectCode( @PathParam( "projectName" ) String projectName) {

		Project project = protoCache.get( projectName );

		project.setCode( project.getCode() + 1 );
		protoCache.put( projectName, project );

		log.info( "Created new project {} with code 1 {}", projectName, project );

		return project;

	}

}
