package it.redhat.demo.cache;

import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.Flag;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import org.slf4j.Logger;

import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@Singleton
@Startup
public class CacheProducer {

	private static final String CACHE_NAME = "projects";

	@Inject
	private Logger log;

	private boolean updateServer = false;

	@Inject
	@JBossMarshalling
	private RemoteCacheManager jbossCacheManager;

	@Inject
	@JBossMarshalling
	private RemoteCacheManager tempJbossCacheManager;

	@Inject
	@ProtoStream
	private RemoteCacheManager protoCacheManager;

	@PostConstruct
	private void onStartup() {

		log.info( "Staring Service..." );

		jbossCacheManager.start();
		protoCacheManager.start();
	}

	@PreDestroy
	private void onShutdown() {

		log.info( "Shutting down Service..." );

		jbossCacheManager.stop();
		protoCacheManager.stop();
	}

	@Produces
	@JBossMarshalling
	public RemoteCache<String, Project> getJbossCache() {
		if (!updateServer) {
			registerClassMarshallerOnServer();
		}

		log.trace( "simple remote cache {} :: produce", CACHE_NAME );
		return jbossCacheManager.<String, Project>getCache( CACHE_NAME ).withFlags( Flag.FORCE_RETURN_VALUE );

	}

	@Produces
	@ProtoStream
	public RemoteCache<String, Project> getProtoCache() {
		if (!updateServer) {
			registerClassMarshallerOnServer();
		}

		log.trace( "simple remote cache {} :: produce", CACHE_NAME );
		return protoCacheManager.<String, Project>getCache( CACHE_NAME ).withFlags( Flag.FORCE_RETURN_VALUE );

	}

	private void registerClassMarshallerOnServer() {
		tempJbossCacheManager.start();
		RemoteCache<Object, Object> cache = null;

		try {
			cache = tempJbossCacheManager.getCache( CACHE_NAME );
			cache.execute( "AddProtobufTask", Collections.emptyMap() );

		} finally {
			if (cache != null) {
				cache.clear();
			}

			tempJbossCacheManager.stop();
		}

		updateServer = true;
	}

}
