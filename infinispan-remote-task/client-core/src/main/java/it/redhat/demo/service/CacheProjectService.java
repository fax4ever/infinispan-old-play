package it.redhat.demo.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;

import it.redhat.demo.cache.ProtoStream;
import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@Stateless
@ProtoStream
public class CacheProjectService implements ProjectService {

	@Inject
	@ProtoStream
	private RemoteCache<String, Project> protoCache;

	@Override
	public Project take(String name) {
		return protoCache.get( name );
	}

	@Override
	public Project create(String name) {
		Project project = new Project();
		project.setCode( 1 );
		project.setDescription( name );
		project.setName( name );

		protoCache.put( name, project );
		return protoCache.get( name );
	}

	@Override
	public Project update(String name) {
		Project project = protoCache.get( name );

		project.setCode( project.getCode() + 1 );
		protoCache.put( name, project );
		return protoCache.get( name );
	}

	@Override
	public void massiveCreate(String hash, int times) {

		for (int i=1; i<=times; i++) {

			String name = hash + i;

			Project project = new Project();
			project.setCode( i );
			project.setDescription( name );
			project.setName( name );

			protoCache.put( name, project );

		}

	}

}
