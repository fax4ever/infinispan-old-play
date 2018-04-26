package it.redhat.demo.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.client.hotrod.RemoteCache;

import it.redhat.demo.cache.CacheRepo;
import it.redhat.demo.model.Movie;
import it.redhat.demo.model.SimpleEntity;

@RunWith(JUnit4.class)
public class QueryTest {

	static CacheRepo cacheRepo = new CacheRepo();

	@BeforeClass
	public static void beforeAll() {
		cacheRepo.init();

		RemoteCache<String, SimpleEntity> simpleEntityCache = cacheRepo.getSimpleEntityCache();
		simpleEntityCache.put( "A", new SimpleEntity( 1, "A", 10l ) );
		simpleEntityCache.put( "B", new SimpleEntity( 1, "B", 1l ) );
		simpleEntityCache.put( "C", new SimpleEntity( 2, "B", 10l ) );
		simpleEntityCache.put( "D", new SimpleEntity( 2, "A", 1l ) );
		simpleEntityCache.put( "E", new SimpleEntity( 1, "A", 10l ) );

		RemoteCache<String, Movie> movieCache = cacheRepo.getMovieCache();
		movieCache.put( "A", new Movie( "A", 7, 777l, "yes", "T2", (byte) 1) );
		movieCache.put( "B", new Movie( "B", 8, 777l, "yes", "T1", (byte) 1) );
		movieCache.put( "C", new Movie( "C", 7, 777l, "no", "T1", (byte) 4) );
		movieCache.put( "D", new Movie( "D", 8, 717l, "yes", "T2", (byte) 1) );
		movieCache.put( "E", new Movie( "E", 7, 717l, "no", "T1", (byte) 4) );

		assertEquals( 5, movieCache.size() );
	}

	@AfterClass
	public static void afterAll() throws Exception {
		cacheRepo.clear();
		cacheRepo.close();
	}

	@Test
	public void test() {

	}
}
