package it.redhat.demo.cache;

import java.io.Closeable;
import java.io.IOException;

import org.infinispan.client.hotrod.RemoteCacheManager;

public class CacheManagerHolder implements Closeable {

	protected RemoteCacheManager cacheManager;

	public CacheManagerHolder() {
		cacheManager = new CacheManagerFactory().getStandardCacheManager();
	}

	@Override
	public void close() throws IOException {
		cacheManager.stop();
		cacheManager.close();
	}

}
