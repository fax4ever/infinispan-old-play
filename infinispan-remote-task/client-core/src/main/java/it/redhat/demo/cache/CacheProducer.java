package it.redhat.demo.cache;

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

	@Inject
	@JBossMarshalling
	private RemoteCacheManager cacheManager;

	@PostConstruct
	private void onStartup() {
		log.info( "Staring Service..." );
		cacheManager.start();
	}

	@PreDestroy
	private void onShutdown() {
		log.info( "Shutting Service..." );
		cacheManager.stop();
	}

	@Inject
	@JBossMarshalling
	private RemoteCacheManager jbossCacheManager;

	@Inject
	@ProtoStream
	private RemoteCacheManager protoCacheManager;

	@Produces
	@JBossMarshalling
	public RemoteCache<String, Project> getJbossCache() {

		log.trace( "simple remote cache {} :: produce", CACHE_NAME );
		return jbossCacheManager.<String, Project>getCache( CACHE_NAME ).withFlags( Flag.FORCE_RETURN_VALUE );

	}

	@Produces
	@ProtoStream
	public RemoteCache<String, Project> getProtoCache() {

		log.trace( "simple remote cache {} :: produce", CACHE_NAME );
		return protoCacheManager.<String, Project>getCache( CACHE_NAME ).withFlags( Flag.FORCE_RETURN_VALUE );

	}

}
