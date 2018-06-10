package it.redhat.demo.cache;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;

import org.slf4j.Logger;

import it.redhat.demo.model.Puzzle;

@Singleton
@Startup
public class CacheProducer {

	private static final String OGM_BASIC_CONFIG =
			"<infinispan><cache-container>" +
					"	<distributed-cache-configuration name=\"%s\">" +
					"     <locking isolation=\"REPEATABLE_READ\"/>" +
					"     <transaction locking=\"PESSIMISTIC\" mode=\"NON_DURABLE_XA\" />" +
					"   </distributed-cache-configuration>" +
					"</cache-container></infinispan>";

	public static final String CACHE_NAME = "puzzle";

	@Inject
	private Logger log;

	@Inject
	private RemoteCacheManager cacheManager;

	@PostConstruct
	private void onStartup() {
		cacheManager.administration().createCache( CACHE_NAME, getCacheConfiguration( CACHE_NAME ) );
	}

	@PreDestroy
	private void onShutdown() {
		cacheManager.stop();
	}

	private XMLStringConfiguration getCacheConfiguration(String cacheName) {
		return new XMLStringConfiguration( String.format( OGM_BASIC_CONFIG, cacheName ) );
	}

	@Produces
	public RemoteCache<Integer, Puzzle> producePuzzleCache() {
		log.trace( "Produce a remote cache controller from a remote cache manager");

		return cacheManager.getCache( CACHE_NAME );
	}
}
