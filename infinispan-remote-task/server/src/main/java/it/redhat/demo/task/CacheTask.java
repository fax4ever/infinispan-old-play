package it.redhat.demo.task;

import org.infinispan.Cache;
import org.infinispan.commons.dataconversion.IdentityEncoder;
import org.infinispan.tasks.TaskContext;

/**
 * @author Fabio Massimo Ercoli
 */
public abstract class CacheTask {

	private static final String CACHE_NAME = "projects";

	protected  <K, V> Cache<K, V> getCache(TaskContext ctx) {
		return (Cache<K, V>) ctx.getCache().get().getAdvancedCache()
				.withEncoding(IdentityEncoder.class);
	}

}
