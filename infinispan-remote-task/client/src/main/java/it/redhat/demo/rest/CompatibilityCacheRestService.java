/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.rest;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.infinispan.client.hotrod.RemoteCache;

import org.slf4j.Logger;

import it.redhat.demo.cache.JBossMarshalling;
import it.redhat.demo.cache.ProtoStream;
import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@Path( "compatibility" )
public class CompatibilityCacheRestService {

	@Inject
	private Logger log;

	@Inject
	@ProtoStream
	private RemoteCache<String, Project> protoCache;

	@Inject
	@JBossMarshalling
	private RemoteCache<String, Project> jbossCache;

	@POST
	@Path( "PJ" )
	public Project compatibilityPJ() {

		String key = "PJ";

		protoCache.put( key, createProject() );
		return jbossCache.get( key );

	}

	@POST
	@Path( "JP" )
	public Project compatibilityCP() {

		String key = "JP";

		jbossCache.put( key, createProject() );
		return protoCache.get( key );

	}

	private Project createProject() {
		Project project = new Project();

		project.setCode( 1 );
		project.setName( "Compatibility Project" );
		project.setDescription( "Compatibility Project" );

		return project;
	}

}
