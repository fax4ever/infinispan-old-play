package it.redhat.demo.producer;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.counter.api.Storage;
import org.infinispan.counter.configuration.CounterManagerConfigurationBuilder;
import org.infinispan.counter.configuration.Reliability;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;

/**
 * @author Fabio Massimo Ercoli (C) 2017 Red Hat Inc.
 */
public class CacheManagerProducer {

	public static final String COUNTER_NAME = "HIBERNATE_OGM_SEQUENCE";

	public EmbeddedCacheManager produce() {

		GlobalConfigurationBuilder globalBuilder = GlobalConfigurationBuilder.defaultClusteredBuilder();

		globalBuilder.addModule( CounterManagerConfigurationBuilder.class )
			.numOwner(2)
			.reliability( Reliability.CONSISTENT )
			.addStrongCounter()
				.name( COUNTER_NAME )
				.initialValue( 0 )
				.storage( Storage.VOLATILE );

		Configuration config = new ConfigurationBuilder()
			.transaction()
				.transactionMode( TransactionMode.TRANSACTIONAL )
				.transactionManagerLookup( new GenericTransactionManagerLookup() )
				.useSynchronization( false )
				.lockingMode( LockingMode.PESSIMISTIC )
			.clustering().hash().groups().enabled()
			.locking()
				.concurrencyLevel( 10 )
				.useLockStriping( false )
				.isolationLevel( IsolationLevel.REPEATABLE_READ )
			.build();

		return new DefaultCacheManager( globalBuilder.build(), config );

	}

	// wrong configuration => Cluster Counters requires Transport
	public EmbeddedCacheManager produceNoTransportCache() {

		GlobalConfigurationBuilder globalBuilder = new GlobalConfigurationBuilder().nonClusteredDefault();

		globalBuilder.addModule( CounterManagerConfigurationBuilder.class )
			.numOwner(2)
			.reliability( Reliability.CONSISTENT )
			.addStrongCounter()
				.name( COUNTER_NAME )
				.initialValue( 0 )
				.storage( Storage.VOLATILE );

		return new DefaultCacheManager( globalBuilder.build() );

	}

}
