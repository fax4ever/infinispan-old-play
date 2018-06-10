package it.redhat.demo.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.TransactionMode;

import org.slf4j.Logger;

@ApplicationScoped
public class CacheManagerProducer {

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_HOTROD_PORT = 11372;

	@Inject
	private Logger log;

	@Produces
	public RemoteCacheManager getProtoCacheManager() {
		log.trace( "remote cache manager :: produce" );

		Configuration config = new ConfigurationBuilder()
				.addServer()
				.host( DEFAULT_HOTROD_BIND_ADDRESS )
				.port( DEFAULT_HOTROD_PORT )
				.transaction()
				.transactionMode( TransactionMode.NON_XA )
				.classLoader( CacheManagerProducer.class.getClassLoader() )
				.build();

		return new RemoteCacheManager( config );
	}
}
