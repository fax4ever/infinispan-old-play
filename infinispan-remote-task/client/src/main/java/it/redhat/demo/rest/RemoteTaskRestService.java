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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.infinispan.client.hotrod.RemoteCache;

import org.slf4j.Logger;

import it.redhat.demo.cache.JBossMarshalling;
import it.redhat.demo.model.Project;

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

	@GET
	@Path( "ciao" )
	public Object ciaoRemoteTask() {

		log.info( "execute task CiaoRemoteTask on cache {}", cache.getName() );

		String result = cache.execute( "CiaoRemoteTask", Collections.emptyMap() );

		log.info( "result: {}", result );

		return result;

	}

	@PUT
	@Path( "project/{projectName}" )
	public Integer incrementProjectCode( @PathParam( "projectName" ) String projectName ) {

		log.info( "execute task IncrementProjectCodeTask on cache {} on project {}", cache.getName(), projectName );

		Project result = cache.execute( "IncrementProjectCodeTask", Collections.singletonMap( "name", projectName ) );

		log.info( "result: {}", result );

		return (result == null) ? 0 : result.getCode();

	}

}
