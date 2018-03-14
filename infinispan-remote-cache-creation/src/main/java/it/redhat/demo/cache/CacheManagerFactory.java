package it.redhat.demo.cache;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class CacheManagerFactory {

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_HOTROD_PORT = 11222;

	public RemoteCacheManager getStandardCacheManager() {

		Configuration config = new ConfigurationBuilder()
			.addServer()
				.host(DEFAULT_HOTROD_BIND_ADDRESS).port(DEFAULT_HOTROD_PORT)
			.build();

		return new RemoteCacheManager( config, true );

	}

}
