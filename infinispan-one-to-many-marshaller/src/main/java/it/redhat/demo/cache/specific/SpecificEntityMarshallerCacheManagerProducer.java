package it.redhat.demo.cache.specific;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.protostream.SerializationContext;

import it.redhat.demo.cache.BaseCacheManagerProducer;

/**
 * @author Fabio Massimo Ercoli
 */
@ApplicationScoped
public class SpecificEntityMarshallerCacheManagerProducer extends BaseCacheManagerProducer {

	@Produces
	@UseSpecificEntityMarshaller
	@Override
	public RemoteCacheManager getProtoCacheManager() {
		return super.getProtoCacheManager();
	}

	@Override
	protected void resisterMessageMarshaller(SerializationContext serCtx) {
		serCtx.registerMarshaller( new EmployeeMarshaller() );
		serCtx.registerMarshaller( new CompanyMarshaller() );
	}

}
