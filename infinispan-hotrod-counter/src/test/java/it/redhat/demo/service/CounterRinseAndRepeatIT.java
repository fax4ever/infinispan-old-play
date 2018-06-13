package it.redhat.demo.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.RemoteCounterManagerFactory;
import org.infinispan.counter.api.CounterConfiguration;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.CounterType;
import org.infinispan.counter.api.Storage;
import org.infinispan.counter.api.StrongCounter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

@RunWith(Arquillian.class)
public class CounterRinseAndRepeatIT {

	@Deployment
	public static WebArchive create() {
		return ShrinkWrap
				.create( WebArchive.class, "infinispan-jboss-demo.war" )
				.addPackages( true, "it.redhat.demo" )
				.addAsWebInfResource( new File( "src/main/webapp/WEB-INF/jboss-deployment-structure.xml" ) );
	}

	@Inject
	private RemoteCacheManager cacheManager;

	@Test
	public void test_createAndDropCounter() throws Exception {
		CounterManager counterManager = RemoteCounterManagerFactory.asCounterManager( cacheManager );

		CounterConfiguration consistentAndDurableConfig = CounterConfiguration.builder( CounterType.UNBOUNDED_STRONG )
				.concurrencyLevel( 10 )
				.initialValue( 0 )
				.lowerBound( 0 )
				.upperBound( 1000 )
				.storage( Storage.PERSISTENT )
				.build();

		final String temporaryCounterName = "temporary-counter";

		for (int i=0; i<10; i++) {
			defineTestAndDisposeCounter( counterManager, consistentAndDurableConfig, temporaryCounterName );
		}
	}

	private void defineTestAndDisposeCounter(CounterManager counterManager, CounterConfiguration consistentAndDurableConfig, String temporaryCounterName)
			throws InterruptedException, java.util.concurrent.ExecutionException {
		counterManager.defineCounter( temporaryCounterName, consistentAndDurableConfig );
		StrongCounter strongCounter = counterManager.getStrongCounter( "temporary-counter" );

		assertEquals ( new Long( 1 ), strongCounter.incrementAndGet().get() );
		assertEquals ( new Long( 2 ), strongCounter.incrementAndGet().get() );
		assertEquals ( new Long( 3 ), strongCounter.incrementAndGet().get() );

		counterManager.remove( temporaryCounterName );
	}
}
