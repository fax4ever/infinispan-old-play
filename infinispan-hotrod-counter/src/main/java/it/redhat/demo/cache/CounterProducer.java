package it.redhat.demo.cache;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.RemoteCounterManagerFactory;
import org.infinispan.counter.api.CounterConfiguration;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.CounterType;
import org.infinispan.counter.api.Storage;
import org.infinispan.counter.api.StrongCounter;
import org.infinispan.counter.api.WeakCounter;

@Singleton
@Startup
public class CounterProducer {

	public static final String CONSISTENT_AND_DURABLE_COUNTER = "consistent-and-durable-counter";
	public static final String EVENTUAL_AND_SOFT_COUNTER = "eventual-and-soft-counter";

	@Inject
	private RemoteCacheManager cacheManager;

	private CounterManager counterManager;

	@PostConstruct
	private void onStartup() {
		counterManager = RemoteCounterManagerFactory.asCounterManager( cacheManager );

		CounterConfiguration consistentAndDurableConfig = CounterConfiguration.builder( CounterType.UNBOUNDED_STRONG )
				.concurrencyLevel( 10 )
				.initialValue( 0 )
				.lowerBound( 0 )
				.upperBound( 1000 )
				.storage( Storage.PERSISTENT )
				.build();

		counterManager.defineCounter( CONSISTENT_AND_DURABLE_COUNTER, consistentAndDurableConfig );

		CounterConfiguration eventualAndSoftStateConfig = CounterConfiguration.builder( CounterType.WEAK )
				.concurrencyLevel( 10 )
				.initialValue( 0 )
				.lowerBound( 0 )
				.upperBound( 1000 )
				.storage( Storage.VOLATILE )
				.build();

		counterManager.defineCounter( EVENTUAL_AND_SOFT_COUNTER, eventualAndSoftStateConfig );
	}

	@PreDestroy
	private void onShutdown() {
		counterManager.remove( CONSISTENT_AND_DURABLE_COUNTER );
		counterManager.remove( EVENTUAL_AND_SOFT_COUNTER );
		cacheManager.stop();
	}

	@Produces
	public StrongCounter produceStrongCounter() {
		return counterManager.getStrongCounter( CONSISTENT_AND_DURABLE_COUNTER );
	}

	@Produces
	public WeakCounter produceWeakCounter() {
		return counterManager.getWeakCounter( EVENTUAL_AND_SOFT_COUNTER );
	}
}
