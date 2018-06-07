package it.redhat.demo.model;

import java.io.Serializable;
import java.util.Objects;

import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

@ProtoMessage
public class Puzzle implements Serializable {

	String name;

	public Puzzle() {
	}

	public Puzzle(String name) {
		this.name = name;
	}

	@ProtoField(number = 1, required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Puzzle puzzle = (Puzzle) o;
		return Objects.equals( name, puzzle.name );
	}

	@Override
	public int hashCode() {

		return Objects.hash( name );
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder( "Puzzle{" );
		sb.append( "name='" ).append( name ).append( '\'' );
		sb.append( '}' );
		return sb.toString();
	}
}
