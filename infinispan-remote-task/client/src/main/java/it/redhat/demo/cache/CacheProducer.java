package it.redhat.demo.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.Flag;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import org.slf4j.Logger;

import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@ApplicationScoped
public class CacheProducer {

    private static final String CACHE_NAME = "projects";

    @Inject
    private Logger log;

    @Inject
    private RemoteCacheManager cacheContainer;

    @Produces
    public RemoteCache<String, Project> getCache() {

        log.trace( "simple remote cache {} :: produce", CACHE_NAME );
        return cacheContainer.<String, Project>getCache( CACHE_NAME ).withFlags( Flag.FORCE_RETURN_VALUE );

    }

}
