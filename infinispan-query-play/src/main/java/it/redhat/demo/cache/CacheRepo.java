package it.redhat.demo.cache;

import java.io.IOException;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;

import it.redhat.demo.model.Movie;
import it.redhat.demo.model.SimpleEntity;

public class CacheRepo {

	private static final String CONFIG = "<infinispan><cache-container>" +
		"<distributed-cache name=\"%s\">" +
		"<expiration interval=\"10000\" lifespan=\"10\" max-idle=\"10\"/>" +
		"</distributed-cache>" +
		"</cache-container></infinispan>";

	private static final String MOVIE_CACHE_NAME = "Movie";
	private static final String SIMPLE_ENTITY_CACHE_NAME = "SimpleEntity";

	private RemoteCacheManager manager;
	private RemoteCache<String, Movie> movieCache;
	private RemoteCache<String, SimpleEntity> simpleEntityCache;

	public CacheRepo() {
		this.manager = new CacheManagerFactory().create();
	}

	public void init() {
		this.movieCache = manager.administration().getOrCreateCache( MOVIE_CACHE_NAME, new XMLStringConfiguration( String.format( CONFIG, MOVIE_CACHE_NAME ) ) );
		this.simpleEntityCache = this.manager.administration().getOrCreateCache( SIMPLE_ENTITY_CACHE_NAME, new XMLStringConfiguration( String.format( CONFIG, SIMPLE_ENTITY_CACHE_NAME ) ) );
	}

	public void clear() {
		movieCache.clear();
		simpleEntityCache.clear();
	}

	public void close() {
		movieCache.stop();
		simpleEntityCache.stop();
		try {
			manager.close();
		}
		catch (IOException e) {
			throw new RuntimeException( e );
		}
	}

	public RemoteCache<String, Movie> getMovieCache() {
		return movieCache;
	}

	public RemoteCache<String, SimpleEntity> getSimpleEntityCache() {
		return simpleEntityCache;
	}
}
