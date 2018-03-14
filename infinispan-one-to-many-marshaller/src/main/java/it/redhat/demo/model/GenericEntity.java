package it.redhat.demo.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GenericEntity {

	Map<String, Object> data = new HashMap<>();

	public GenericEntity put(String key, Object value) {
		data.put( key, value );
		return this;
	}

	public <T> T get(String key) {
		return (T) data.get( key );
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		GenericEntity that = (GenericEntity) o;
		return Objects.equals( data, that.data );
	}

	@Override
	public int hashCode() {
		return Objects.hash( data );
	}

	@Override
	public String toString() {
		return "GenericEntity{" +
				"data=" + data +
				'}';
	}

}
