package it.redhat.demo.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.demo.cache.CacheManagerFactory;
import it.redhat.demo.cache.CacheRepo;
import it.redhat.demo.helper.ByteArrayHelper;
import it.redhat.demo.model.Movie;
import it.redhat.demo.model.Simple;

@RunWith(JUnit4.class)
public class QueryIT {

	private static final Logger LOG = LoggerFactory.getLogger( QueryIT.class );

	RemoteCacheManager cacheManager;
	CacheRepo cacheRepo;

	RemoteCache<String, Movie> movieCache;
	RemoteCache<String, Simple> simpleCache;

	@Before
	public void before() {
		cacheManager = new CacheManagerFactory().create();
		cacheRepo = new CacheRepo( cacheManager );
		cacheRepo.create();

		movieCache = cacheRepo.getMovieCache();
		movieCache.put( "A", new Movie( "A", 7, 777l, "yes", "T2", (byte) 1 ) );
		movieCache.put( "B", new Movie( "B", 8, 777l, "yes", "T1", (byte) 2 ) );
		movieCache.put( "C", new Movie( "C", 7, 777l, "no", "T1", (byte) 4 ) );
		movieCache.put( "D", new Movie( "D", 8, 717l, "yes", "T2", (byte) 1 ) );
		movieCache.put( "E", new Movie( "E", 7, 717l, "no", "T1", (byte) 4 ) );
		movieCache.put( "F", null );
		movieCache.put( "G", null );

		simpleCache = cacheRepo.getSimpleCache();
		simpleCache.put( "A", new Simple( 1, "A", 10l ) );
		simpleCache.put( "B", new Simple( 1, "B", 1l ) );
		simpleCache.put( "C", new Simple( 2, "B", 10l ) );
		simpleCache.put( "D", new Simple( 2, "A", 1l ) );
		simpleCache.put( "E", new Simple( 1, "A", 10l ) );
		simpleCache.put( "F", null );
		simpleCache.put( "G", null );

		LOG.info( "movieCache size: {}", movieCache.size() );
		LOG.info( "movieCache size: {}", simpleCache.size() );
	}

	@After
	public void after() throws Exception {
		cacheRepo.dispose();
		cacheManager.close();

		cacheManager = null;
		cacheRepo = null;

		movieCache = null;
		simpleCache = null;
	}

	@Test
	public void getBulk_simple() {
		QueryFactory qf = Search.getQueryFactory( simpleCache );
		Query query = qf.from( Simple.class ).build();
		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 5, output.size() );
	}

	@Test
	public void getBulk_movie() {
		QueryFactory qf = Search.getQueryFactory( movieCache );
		Query query = qf.create( "FROM ProtoModel.Movie" );
		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 5, output.size() );
	}

	@Test
	public void findMovieBygenre() {
		QueryFactory qf = Search.getQueryFactory( movieCache );
		Query query = qf.create( "from ProtoModel.Movie where genre = 7" );
		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 3, output.size() );
	}

	@Test(expected = HotRodClientException.class)
	public void findMovieByviewerRating() {
		QueryFactory qf = Search.getQueryFactory( movieCache );
		Query query = qf.create( "from ProtoModel.Movie where viewerRating = 4" );
		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 2, output.size() );
	}

	@Test(expected = HotRodClientException.class)
	public void findMovieByviewerRating_withParams_byteArray() {
		QueryFactory qf = Search.getQueryFactory( movieCache );

		Query query = qf.create( "from ProtoModel.Movie where viewerRating = :viewerRating" )
				.setParameter( "viewerRating", (byte) 4 );

		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 2, output.size() );
	}

	@Test(expected = HotRodClientException.class)
	public void findMovieByviewerRating_withParams_byteArrays() {
		QueryFactory qf = Search.getQueryFactory( movieCache );

		byte[] bytes = ByteArrayHelper.toArray( (byte) 4 );

		Query query = qf.create( "from ProtoModel.Movie where viewerRating = :viewerRating" )
				.setParameter( "viewerRating", bytes );

		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 2, output.size() );
	}

	@Test(expected = HotRodClientException.class)
	public void findMovieByviewerRating_IN_listOfbyteArrays() {
		QueryFactory qf = Search.getQueryFactory( movieCache );

		ArrayList<Object> params = new ArrayList<>();
		params.add( ByteArrayHelper.toArray( (byte) 4 ) );
		params.add( ByteArrayHelper.toArray( (byte) 1 ) );

		Query query = qf.create( "from ProtoModel.Movie where viewerRating in (:viewerRating)" )
				.setParameter( "viewerRating", params );

		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 2, output.size() );
	}

	@Test
	public void findMovieBysuitableForKids_withParameters() {
		QueryFactory qf = Search.getQueryFactory( movieCache );

		ArrayList<Object> params = new ArrayList<>();
		params.add( ByteArrayHelper.toArray( (byte) 4 ) );
		params.add( ByteArrayHelper.toArray( (byte) 1 ) );

		Query query = qf.create( "from ProtoModel.Movie where suitableForKids = :suitable" )
				.setParameter( "suitable", "yes" );

		List<Object> output = query.list();

		LOG.info( "Query {}: output {}", query, output );
		assertEquals( 3, output.size() );
	}

}
