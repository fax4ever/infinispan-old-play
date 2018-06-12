package it.redhat.demo.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

@ApplicationScoped
public class CacheManagerProducer {

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_HOTROD_PORT = 11372;

	@Produces
	public RemoteCacheManager getProtoCacheManager() {
		Configuration config = new ConfigurationBuilder()
				.addServer()
				.host( DEFAULT_HOTROD_BIND_ADDRESS )
				.port( DEFAULT_HOTROD_PORT )
				.transaction()
				.classLoader( CacheManagerProducer.class.getClassLoader() )
				.build();

		return new RemoteCacheManager( config );
	}
}
