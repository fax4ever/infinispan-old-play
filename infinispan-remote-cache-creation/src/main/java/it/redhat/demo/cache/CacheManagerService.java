package it.redhat.demo.cache;

import org.infinispan.client.hotrod.RemoteCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManagerService extends CacheManagerHolder {

	private static final Logger LOG = LoggerFactory.getLogger( CacheManagerService.class );

	public boolean testCacheExists(String cacheName) {

		LOG.info( "Testing cache: {}", cacheName );
		RemoteCache<Object, Object> cache = cacheManager.getCache( cacheName );

		if (cache != null) {
			LOG.info( "Cache {}: Exists", cache.getName() );
			return true;
		} else {
			LOG.info( "Cache {}: Does Not Exist", cacheName );
			return false;
		}

	}

	public boolean createCacheFrom(String baseName, String templateName) {

		String cacheName = baseName + "-" + templateName;
		LOG.info( "Trying create cache <{}>, using <{}> as template", cacheName, templateName );

		try {
			RemoteCache<Object, Object> cache = cacheManager.administration().createCache( cacheName, templateName );
			LOG.info( "Cache <{}>: Created", cache.getName() );
			return true;
		} catch (RuntimeException ex) {
			LOG.info( "Cache <{}>: Not Created. Exception <{}> message: <{}>.", cacheName, ex.getClass(), ex.getMessage() );
			return false;
		}

	}

}
