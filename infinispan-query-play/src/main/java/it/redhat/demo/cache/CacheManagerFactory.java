package it.redhat.demo.cache;

import java.io.IOException;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.demo.model.MovieMarshaller;
import it.redhat.demo.model.SimpleEntityMarshaller;

public class CacheManagerFactory {

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_HOTROD_PORT = 11222;
	private static final String PROTO_FILE_NAME = "model.proto";

	private static final Logger LOG = LoggerFactory.getLogger( CacheManagerFactory.class );

	public RemoteCacheManager create() {

		ProtoStreamMarshaller marshaller = new ProtoStreamMarshaller();

		Configuration config = new ConfigurationBuilder()
				.marshaller( marshaller )
				.addServer()
				.host( DEFAULT_HOTROD_BIND_ADDRESS )
				.port( DEFAULT_HOTROD_PORT )
				.build();

		RemoteCacheManager remoteCacheManager = new RemoteCacheManager( config );
		SerializationContext serCtx = marshaller.getSerializationContext();
		registerProtoFiles( serCtx );
		serCtx.registerMarshaller( new SimpleEntityMarshaller() );
		serCtx.registerMarshaller( new MovieMarshaller() );

		return remoteCacheManager;

	}

	private void registerProtoFiles(SerializationContext serCtx) {
		try {
			serCtx.registerProtoFiles( FileDescriptorSource.fromResources( PROTO_FILE_NAME ) );
		}
		catch (IOException e) {
			throw new IllegalStateException( "An error occurred initializing ProtoStream protobuf marshaller:", e );
		}
	}
}
