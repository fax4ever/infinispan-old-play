package it.redhat.demo.cache;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.TransactionMode;
import org.infinispan.commons.marshall.jboss.GenericJBossMarshaller;

public class CacheManagerFactory {

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_HOTROD_PORT = 11222;

	public static final TransactionMode TRANSACTION_MODE = TransactionMode.NON_XA;

	public RemoteCacheManager create() {
		Configuration config = new ConfigurationBuilder()
				.marshaller( new GenericJBossMarshaller() )
				.addServer()
				.host( DEFAULT_HOTROD_BIND_ADDRESS )
				.port( DEFAULT_HOTROD_PORT )
				.transaction()
				.transactionMode( TRANSACTION_MODE )
				.build();

		return new RemoteCacheManager( config );
	}
}
