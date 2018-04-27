package it.redhat.demo.cache;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;

import it.redhat.demo.model.Movie;
import it.redhat.demo.model.Simple;

public class CacheRepo {

	public static final String CONFIG =
			"<infinispan><cache-container>" +
			"	<distributed-cache-configuration name=\"%s\">" +
			"     <locking striping=\"false\" acquire-timeout=\"10000\" concurrency-level=\"50\" isolation=\"READ_COMMITTED\"/>" +
			"     <transaction mode=\"NON_DURABLE_XA\" />" +
			"     <expiration max-idle=\"-1\" />" +
			"     <indexing index=\"NONE\" />" +
			"     <state-transfer timeout=\"480000\" await-initial-transfer=\"true\" />" +
			"   </distributed-cache-configuration>" +
			"</cache-container></infinispan>";

	public static final String MOVIE_CACHE_NAME = "Movie";
	public static final String SIMPLE_ENTITY_CACHE_NAME = "Simple";

	private final RemoteCacheManager manager;
	private RemoteCache<String, Movie> movieCache;
	private RemoteCache<String, Simple> simpleCache;

	public CacheRepo(RemoteCacheManager manager) {
		this.manager = manager;
	}

	public void create() {
		this.movieCache = manager.administration().getOrCreateCache(
				MOVIE_CACHE_NAME, new XMLStringConfiguration( String.format( CONFIG, MOVIE_CACHE_NAME ) ) );
		this.simpleCache = this.manager.administration().getOrCreateCache(
				SIMPLE_ENTITY_CACHE_NAME, new XMLStringConfiguration( String.format( CONFIG, SIMPLE_ENTITY_CACHE_NAME ) ) );
	}

	public void dispose() {
		movieCache.clear();
		simpleCache.clear();
		movieCache.stop();
		simpleCache.stop();

		manager.administration().removeCache( MOVIE_CACHE_NAME );
		manager.administration().removeCache( SIMPLE_ENTITY_CACHE_NAME );
	}

	public RemoteCache<String, Movie> getMovieCache() {
		return movieCache;
	}

	public RemoteCache<String, Simple> getSimpleCache() {
		return simpleCache;
	}
}
