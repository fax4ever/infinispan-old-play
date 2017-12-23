package it.redhat.demo.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.counter.EmbeddedCounterManagerFactory;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.exception.CounterException;
import org.infinispan.manager.EmbeddedCacheManager;

import it.redhat.demo.producer.CacheManagerProducer;

import static it.redhat.demo.producer.CacheManagerProducer.COUNTER_NAME;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * @author Fabio Massimo Ercoli (C) 2017 Red Hat Inc.
 */
@RunWith(JUnit4.class)
public class NoClusterCounterTest {

	private static EmbeddedCacheManager cacheManager;

	@BeforeClass
	public static void beforeClass() {

		cacheManager = new CacheManagerProducer().produceNoTransportCache();

	}

	@AfterClass
	public static void afterClass() {

		cacheManager.stop();

	}

	@Test(expected = CounterException.class)
	public void test_name_ok() {

		assertNotNull( cacheManager );
		CounterManager counterManager = EmbeddedCounterManagerFactory.asCounterManager( cacheManager );
		assertNotNull( counterManager );
		counterManager.getStrongCounter( COUNTER_NAME );
		fail();

	}

}
