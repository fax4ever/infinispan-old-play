package it.redhat.demo.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.counter.EmbeddedCounterManagerFactory;
import org.infinispan.counter.api.CounterConfiguration;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.CounterType;
import org.infinispan.counter.api.Storage;
import org.infinispan.counter.api.StrongCounter;
import org.infinispan.counter.exception.CounterException;
import org.infinispan.manager.EmbeddedCacheManager;

import it.redhat.demo.producer.CacheManagerProducer;

import static it.redhat.demo.producer.CacheManagerProducer.COUNTER_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Fabio Massimo Ercoli (C) 2017 Red Hat Inc.
 */
@RunWith(JUnit4.class)
public class ClusterCounterTest {

	private static EmbeddedCacheManager cacheManager;

	@BeforeClass
	public static void beforeClass() {

		cacheManager = new CacheManagerProducer().produce();

	}

	@AfterClass
	public static void afterClass() {

		cacheManager.stop();

	}

	@Test
	public void test_name_ok() {

		assertNotNull( cacheManager );
		CounterManager counterManager = EmbeddedCounterManagerFactory.asCounterManager( cacheManager );
		assertNotNull( counterManager );
		StrongCounter strongCounter = counterManager.getStrongCounter( COUNTER_NAME );
		assertNotNull( strongCounter );

	}

	@Test(expected = CounterException.class)
	public void test_name_ko() {

		CounterManager counterManager = EmbeddedCounterManagerFactory.asCounterManager( cacheManager );
		counterManager.getStrongCounter( COUNTER_NAME + "_KO" );
		fail();

	}

	@Test
	public void test_generateId() throws Exception {

		// given a strong counter
		CounterManager counterManager = EmbeddedCounterManagerFactory.asCounterManager( cacheManager );
		StrongCounter strongCounter = counterManager.getStrongCounter( COUNTER_NAME );

		// when increment and get
		Long newValue = strongCounter.incrementAndGet().get();
		// then 1
		assertEquals( 1l, newValue.longValue() );

		// when increment and get
		newValue = strongCounter.incrementAndGet().get();
		// then 2
		assertEquals( 2l, newValue.longValue() );

		// when increment and get
		newValue = strongCounter.incrementAndGet().get();
		// then 3
		assertEquals( 3l, newValue.longValue() );

	}

	@Test
	public void test_runtimeCounter_generateId() throws Exception {

		// given a strong counter
		CounterManager counterManager = EmbeddedCounterManagerFactory.asCounterManager( cacheManager );

		// adding a runtime counter
		final String hibernate_ogm_runtime_counter = "HIBERNATE_OGM_RUNTIME_COUNTER";

		counterManager.defineCounter( hibernate_ogm_runtime_counter,
			CounterConfiguration.builder( CounterType.UNBOUNDED_STRONG )
				.initialValue( 0 )
				.storage( Storage.VOLATILE )
			.build());

		StrongCounter strongCounter = counterManager.getStrongCounter( hibernate_ogm_runtime_counter );

		// when increment and get
		Long newValue = strongCounter.incrementAndGet().get();
		// then 1
		assertEquals( 1l, newValue.longValue() );

		// when increment and get
		newValue = strongCounter.incrementAndGet().get();
		// then 2
		assertEquals( 2l, newValue.longValue() );

		// when increment and get
		newValue = strongCounter.incrementAndGet().get();
		// then 3
		assertEquals( 3l, newValue.longValue() );

	}

	@Test
	public void test_runtimeCounter_defineTwoTimes() throws Exception {

		// given a strong counter
		CounterManager counterManager = EmbeddedCounterManagerFactory.asCounterManager( cacheManager );

		// adding a runtime counter
		final String hibernate_ogm_runtime_counter = "HIBERNATE_OGM_RUNTIME_COUNTER_2";

		boolean outcome = counterManager.defineCounter(
			hibernate_ogm_runtime_counter,
				CounterConfiguration.builder( CounterType.UNBOUNDED_STRONG )
					.initialValue( 0 )
					.storage( Storage.VOLATILE )
					.build()
		);

		assertTrue(outcome);

		outcome = counterManager.defineCounter( hibernate_ogm_runtime_counter,
			  CounterConfiguration.builder( CounterType.UNBOUNDED_STRONG )
				  .initialValue( 0 )
				  .storage( Storage.VOLATILE )
				  .build());

		assertFalse(outcome);

	}

}
