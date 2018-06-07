package it.redhat.demo.cache;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import org.slf4j.Logger;

import it.redhat.demo.model.Puzzle;

@Singleton
@Startup
public class CacheProducer {

	private static final String CACHE_NAME = "puzzle";

	@Inject
	private Logger log;

	@Inject
	private RemoteCacheManager cacheManager;

	@PostConstruct
	private void onStartup() {
		cacheManager.administration().getOrCreateCache( CACHE_NAME, (String) null );
	}

	@PreDestroy
	private void onShutdown() {
		cacheManager.stop();
	}

	@Produces
	public RemoteCache<Integer, Puzzle> producePuzzleCache() {
		return cacheManager.getCache( CACHE_NAME );
	}
}
