package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class SimpleEntityMarshaller implements MessageMarshaller<SimpleEntity> {

	public SimpleEntity readFrom(ProtoStreamReader reader) throws IOException {
		Integer value1 = reader.readInt( "value1" );
		String value2 = reader.readString( "value2" );
		Long value3 = reader.readLong( "value3" );

		return new SimpleEntity( value1, value2, value3 );
	}

	public void writeTo(ProtoStreamWriter writer, SimpleEntity entity) throws IOException {
		writer.writeInt( "value1", entity.value1 );
		writer.writeString( "value2", entity.value2 );
		writer.writeLong( "value3", entity.value3 );
	}

	public Class<? extends SimpleEntity> getJavaClass() {
		return SimpleEntity.class;
	}

	public String getTypeName() {
		return "ProtoModel.SimpleEntity";
	}
}
