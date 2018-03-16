package it.redhat.demo.cache.listener;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.client.hotrod.annotation.ClientCacheEntryCreated;
import org.infinispan.client.hotrod.annotation.ClientListener;
import org.infinispan.client.hotrod.event.ClientCacheEntryCreatedEvent;

import org.slf4j.Logger;

@ClientListener
@ApplicationScoped
public class EmployeeCreationListener {

	@Inject
	private Logger log;

	@ClientCacheEntryCreated
	public void handleCreatedEvent(ClientCacheEntryCreatedEvent e) {
		log.info( "new Entry {}" + e );
	}

}
