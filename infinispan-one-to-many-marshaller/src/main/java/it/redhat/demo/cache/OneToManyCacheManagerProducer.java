package it.redhat.demo.cache;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;

import org.slf4j.Logger;

import it.redhat.demo.model.Company;
import it.redhat.demo.model.CompanyMarshaller;
import it.redhat.demo.model.Employee;
import it.redhat.demo.model.EmployeeMarshaller;
import it.redhat.demo.model.GenericEntityMarshaller;

/**
 * @author Fabio Massimo Ercoli
 */
@ApplicationScoped
public class OneToManyCacheManagerProducer {

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_HOTROD_PORT = 11372;

    // name must have '.proto' extension
	private static final String PROTO_SCHEMA_NAME = "cicciolippo.proto";

	@Inject
    private Logger log;

    @Produces
	@OneToManyMarshaller
    public RemoteCacheManager getProtoCacheManager() {

		log.trace("remote cache manager :: produce");

		ProtoStreamMarshaller marshaller = new ProtoStreamMarshaller();

		Configuration config = new ConfigurationBuilder()
			.marshaller( marshaller )
			.addServer()
				.host( DEFAULT_HOTROD_BIND_ADDRESS )
				.port( DEFAULT_HOTROD_PORT )
			.build();

		RemoteCacheManager remoteCacheManager = new RemoteCacheManager( config );
		configureProtoMarshaller( marshaller.getSerializationContext(), remoteCacheManager );

		return remoteCacheManager;

    }

	private void configureProtoMarshaller(SerializationContext serCtx, RemoteCacheManager remoteCacheManager) {
		ProtoSchemaBuilder protoSchemaBuilder = new ProtoSchemaBuilder();
		try {
			String generatedSchema = protoSchemaBuilder.fileName( PROTO_SCHEMA_NAME )
				.packageName( Employee.class.getPackage().getName() )
					.addClass( Employee.class )
					.addClass( Company.class )
				.build( serCtx );

			log.info( "/n--- PROTO SCHEMA ---/n" + generatedSchema );

			serCtx.registerMarshaller( new GenericEntityMarshaller( Employee.class.getName(), getEmployeeMetadata() ) );
			serCtx.registerMarshaller( new GenericEntityMarshaller( Company.class.getName(), getCompanyMetadata() ) );

			String cacheName = ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME;
			RemoteCache<String, String> metadataCache = remoteCacheManager.getCache( cacheName );
			metadataCache.put(PROTO_SCHEMA_NAME, generatedSchema);

			String errors = metadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);
			if (errors != null) {
				throw new IllegalStateException("Some Protobuf schema files contain errors:\n" + errors);
			}

		} catch (Exception e) {
			throw new IllegalStateException("An error occurred initializing ProtoStream protobuf marshaller:", e);
		}
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
