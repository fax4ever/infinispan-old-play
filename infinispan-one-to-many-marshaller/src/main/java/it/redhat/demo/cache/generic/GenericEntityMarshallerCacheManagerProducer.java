package it.redhat.demo.cache.generic;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.protostream.SerializationContext;

import it.redhat.demo.cache.BaseCacheManagerProducer;
import it.redhat.demo.model.Company;
import it.redhat.demo.model.Employee;

/**
 * @author Fabio Massimo Ercoli
 */
@ApplicationScoped
public class GenericEntityMarshallerCacheManagerProducer extends BaseCacheManagerProducer {

	@Produces
	@UseGenericEntityMarshaller
	@Override
	public RemoteCacheManager getProtoCacheManager() {
		return super.getProtoCacheManager();
	}

	@Override
	protected void resisterMessageMarshaller(SerializationContext serCtx) {
		serCtx.registerMarshaller( new GenericEntityMarshaller( Employee.class.getName(), getEmployeeMetadata() ) );
		serCtx.registerMarshaller( new GenericEntityMarshaller( Company.class.getName(), getCompanyMetadata() ) );
	}

	private Map<String,Class> getCompanyMetadata() {
		HashMap<String, Class> metadata = new HashMap<>();

		metadata.put( "name", String.class );
		metadata.put( "country", String.class );
		metadata.put( "vat", String.class );

		return metadata;
	}

	private Map<String,Class> getEmployeeMetadata() {
		HashMap<String, Class> metadata = new HashMap<>();

		metadata.put( "code", Integer.class );
		metadata.put( "name", String.class );
		metadata.put( "surname", String.class );
		metadata.put( "company", String.class );

		return metadata;
	}

}
