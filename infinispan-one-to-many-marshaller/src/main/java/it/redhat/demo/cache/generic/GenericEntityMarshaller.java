package it.redhat.demo.cache.generic;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.infinispan.protostream.MessageMarshaller;

import it.redhat.demo.model.GenericEntity;

public class GenericEntityMarshaller implements MessageMarshaller<GenericEntity> {

	private final Map<String, Class> metadata;
	private final String typeName;

	public GenericEntityMarshaller(String typeName, Map<String, Class> metadata) {
		this.metadata = Collections.unmodifiableMap( metadata );
		this.typeName = typeName;
	}

	@Override
	public GenericEntity readFrom(ProtoStreamReader reader) throws IOException {

		GenericEntity genericEntity = new GenericEntity();

		for (Map.Entry<String, Class> fieldMetadata : metadata.entrySet()) {
			String fieldName = fieldMetadata.getKey();
			Class fieldType = fieldMetadata.getValue();

			if ( String.class.equals( fieldType )) {
				String value = reader.readString( fieldName );
				genericEntity.put( fieldName, value );
			}
			else if ( Integer.class.equals( fieldType )) {
				Integer value = reader.readInt( fieldName );
				genericEntity.put( fieldName, value );
			}
		}

		return genericEntity;
	}

	@Override
	public void writeTo(ProtoStreamWriter writer, GenericEntity genericEntity) throws IOException {

		for (Map.Entry<String, Class> fieldMetadata : metadata.entrySet()) {
			String fieldName = fieldMetadata.getKey();
			Class fieldType = fieldMetadata.getValue();

			if ( String.class.equals( fieldType )) {
				writer.writeString( fieldName, genericEntity.get( fieldName ) );
			}
			else if ( Integer.class.equals( fieldType )) {
				writer.writeInt( fieldName, genericEntity.get( fieldName ) );
			}
		}

	}

	@Override
	public Class<? extends GenericEntity> getJavaClass() {
		return GenericEntity.class;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

}
