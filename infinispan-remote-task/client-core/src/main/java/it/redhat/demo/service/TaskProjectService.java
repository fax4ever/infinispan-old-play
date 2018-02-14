package it.redhat.demo.service;

import java.util.Collections;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;

import it.redhat.demo.cache.JBossMarshalling;
import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@Stateless
@JBossMarshalling
public class TaskProjectService implements ProjectService {

	public static final String NAME_PARAM = "name";

	// remote task invocation needs default marshaller
	// see https://issues.jboss.org/browse/ISPN-8020
	@Inject
	@JBossMarshalling
	private RemoteCache<String, Project> cache;

	@Override
	public Project take(String name) {
		return cache.execute( "GetProjectByNameTask", Collections.singletonMap( NAME_PARAM, name ) );
	}

	@Override
	public Project create(String name) {
		return cache.execute( "CreateProjectByNameTask", Collections.singletonMap( "name", name ) );
	}

	@Override
	public Project update(String name) {
		return cache.execute( "IncrementProjectCodeTask", Collections.singletonMap( "name", name ) );
	}

}
