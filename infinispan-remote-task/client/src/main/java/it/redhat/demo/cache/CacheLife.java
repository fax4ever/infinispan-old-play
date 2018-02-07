package it.redhat.demo.cache;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;

import org.slf4j.Logger;

/**
 * @author Fabio Massimo Ercoli
 */
@Singleton
@Startup
public class CacheLife {

	@Inject
	private Logger log;

	@Inject
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

}
