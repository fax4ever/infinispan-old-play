/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.rest;

import java.util.HashMap;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.infinispan.client.hotrod.RemoteCache;

import org.slf4j.Logger;

/**
 * @author Fabio Massimo Ercoli
 */
@Path( "/" )
@Stateless
public class TaskCallService {

	public static final String REMOTE_TASK_NAME = "myRemoteTask";

	@Inject
	private Logger log;

	@Inject
	private RemoteCache<String, String> cache;

	@GET
	public String ciao() {
		return "ciao";
	}

	@GET
	@Path( "task" )
	public void invokeRemoteTask() {

		log.info( "execute task {} on cache: {}", REMOTE_TASK_NAME, cache.getName() );
		HashMap params = new HashMap();
		Object result = cache.execute( REMOTE_TASK_NAME, params );
		log.info("with outcome {}", result);

	}

}
