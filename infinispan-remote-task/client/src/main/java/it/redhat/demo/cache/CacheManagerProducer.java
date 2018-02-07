package it.redhat.demo.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import org.slf4j.Logger;

/**
 * @author Fabio Massimo Ercoli
 */
@ApplicationScoped
public class CacheManagerProducer {

    private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_HOTROD_PORT = 11322;

    private Logger log;

    @Produces
    public RemoteCacheManager getCacheManager() {

        Configuration config = new ConfigurationBuilder()
			.addServer()
				.host(DEFAULT_HOTROD_BIND_ADDRESS).port(DEFAULT_HOTROD_PORT)
			.build();

        //log.trace("remote cache manager :: produce");

        return new RemoteCacheManager( config );

    }


}
