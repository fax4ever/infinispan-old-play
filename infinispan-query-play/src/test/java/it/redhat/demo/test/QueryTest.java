package it.redhat.demo.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.client.hotrod.RemoteCacheManager;

import it.redhat.demo.cache.CacheManagerFactory;

@RunWith(JUnit4.class)
public class QueryTest {

	public static RemoteCacheManager cacheManager;

	@BeforeClass
	public static void beforeAll() {
		CacheManagerFactory cacheManagerFactory = new CacheManagerFactory();
		cacheManager = cacheManagerFactory.create();
	}

	@AfterClass
	public static void afterAll() throws Exception {
		if ( cacheManager != null ) {
			cacheManager.close();
		}
	}

	@Test
	public void test() {

	}
}
