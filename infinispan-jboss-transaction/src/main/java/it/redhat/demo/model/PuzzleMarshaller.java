package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class PuzzleMarshaller implements MessageMarshaller<Puzzle> {

	@Override
	public Puzzle readFrom(ProtoStreamReader reader) throws IOException {
		String name = reader.readString( "name" );
		return new Puzzle( name );
	}

	@Override
	public void writeTo(ProtoStreamWriter writer, Puzzle o) throws IOException {
		writer.writeString( "name", o.name );
	}

	@Override
	public Class getJavaClass() {
		return Puzzle.class;
	}

	@Override
	public String getTypeName() {
		return Puzzle.class.getName();
	}
}
