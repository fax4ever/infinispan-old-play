package it.redhat.demo.model;

import java.io.Serializable;

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
}
