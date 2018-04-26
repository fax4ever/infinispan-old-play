package it.redhat.demo.cache;

import java.io.IOException;
import java.io.InputStream;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.demo.model.MovieMarshaller;
import it.redhat.demo.model.SimpleEntityMarshaller;

public class CacheManagerFactory {

	private static final Logger LOG = LoggerFactory.getLogger( CacheManagerFactory.class );

	private static final String DEFAULT_HOTROD_BIND_ADDRESS = "127.0.0.1";
	private static final int DEFAULT_HOTROD_PORT = 11222;
	private static final String PROTO_FILE_NAME = "model.proto";

	private String protoSchema;

	public RemoteCacheManager create() {

		ProtoStreamMarshaller marshaller = new ProtoStreamMarshaller();

		Configuration config = new ConfigurationBuilder()
				.marshaller( marshaller )
				.addServer()
				.host( DEFAULT_HOTROD_BIND_ADDRESS )
				.port( DEFAULT_HOTROD_PORT )
				.build();

		RemoteCacheManager remoteCacheManager = new RemoteCacheManager( config );
		loadSchema();
		LOG.info( protoSchema );

		SerializationContext serCtx = marshaller.getSerializationContext();
		registerProtoFilesOnClient( serCtx );
		serCtx.registerMarshaller( new SimpleEntityMarshaller() );
		serCtx.registerMarshaller( new MovieMarshaller() );

		String cacheName = ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME;
		RemoteCache<String, String> metadataCache = remoteCacheManager.getCache( cacheName );
		metadataCache.put( PROTO_FILE_NAME, protoSchema );

		return remoteCacheManager;

	}

	private void registerProtoFilesOnClient(SerializationContext serCtx) {
		try {
			serCtx.registerProtoFiles( FileDescriptorSource.fromString( PROTO_FILE_NAME, protoSchema ) );
		}
		catch (IOException e) {
			throw new IllegalStateException( "An error occurred initializing ProtoStream protobuf marshaller:", e );
		}
	}

	private void loadSchema() {
		try ( InputStream is = this.getClass().getClassLoader().getResourceAsStream( PROTO_FILE_NAME ) ) {
			java.util.Scanner s = new java.util.Scanner( is ).useDelimiter( "\\A" );
			protoSchema = s.hasNext() ? s.next() : "";
		}
		catch (IOException e) {
			throw new IllegalStateException( "An error occurred initializing ProtoStream protobuf marshaller:", e );
		}
	}
}
