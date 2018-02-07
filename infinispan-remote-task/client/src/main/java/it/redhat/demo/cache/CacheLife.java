package it.redhat.demo.cache;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;

/**
 * @author Fabio Massimo Ercoli
 */
@Singleton
@Startup
public class CacheLife {

	private Logger log;

	@Inject
	private RemoteCacheManager cacheManager;

	@PostConstruct
	private void onStartup() {
		//log.info( "Staring Service..." );
		cacheManager.start();
	}

	@PreDestroy
	private void onShutdown() {
		//log.info( "Shutting Service..." );
		cacheManager.stop();
	}

}
