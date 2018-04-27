package it.redhat.demo.model;

import java.util.Objects;

public class Simple {

	Integer value1;

	String value2;

	Long value3;

	public Simple() {
	}

	public Simple(Integer value1, String value2, Long value3) {
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Simple that = (Simple) o;
		return Objects.equals( value1, that.value1 ) &&
				Objects.equals( value2, that.value2 ) &&
				Objects.equals( value3, that.value3 );
	}

	@Override
	public int hashCode() {
		return Objects.hash( value1, value2, value3 );
	}

	@Override
	public String toString() {
		return "Simple{" +
				"value1=" + value1 +
				", value2='" + value2 + '\'' +
				", value3=" + value3 +
				'}';
	}
}
