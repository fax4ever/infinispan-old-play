package it.redhat.demo.test;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import it.redhat.demo.cache.CacheRepo;
import it.redhat.demo.model.Movie;
import it.redhat.demo.model.SimpleEntity;

@RunWith(JUnit4.class)
public class QueryIT {

	static CacheRepo cacheRepo = new CacheRepo();
	static RemoteCacheManager manager;

	@BeforeClass
	public static void beforeAll() {
		cacheRepo.init();
		manager = cacheRepo.getManager();

		RemoteCache<String, SimpleEntity> simpleEntityCache = manager.getCache( CacheRepo.SIMPLE_ENTITY_CACHE_NAME );
		simpleEntityCache.put( "A", new SimpleEntity( 1, "A", 10l ) );
		simpleEntityCache.put( "B", new SimpleEntity( 1, "B", 1l ) );
		simpleEntityCache.put( "C", new SimpleEntity( 2, "B", 10l ) );
		simpleEntityCache.put( "D", new SimpleEntity( 2, "A", 1l ) );
		simpleEntityCache.put( "E", new SimpleEntity( 1, "A", 10l ) );

		RemoteCache<String, Movie> movieCache = manager.getCache( CacheRepo.MOVIE_CACHE_NAME );
		movieCache.put( "A", new Movie( "A", 7, 777l, "yes", "T2", (byte) 1 ) );
		movieCache.put( "B", new Movie( "B", 8, 777l, "yes", "T1", (byte) 1 ) );
		movieCache.put( "C", new Movie( "C", 7, 777l, "no", "T1", (byte) 4 ) );
		movieCache.put( "D", new Movie( "D", 8, 717l, "yes", "T2", (byte) 1 ) );
		movieCache.put( "E", new Movie( "E", 7, 717l, "no", "T1", (byte) 4 ) );
	}

	@AfterClass
	public static void afterAll() throws Exception {
		cacheRepo.clear();
		cacheRepo.close();
	}

	@Test
	public void test() {
		QueryFactory qf = Search.getQueryFactory( manager.getCache( CacheRepo.SIMPLE_ENTITY_CACHE_NAME ) );
		Query query = qf.from( SimpleEntity.class ).build();
		List<Object> list = query.list();
	}
}
