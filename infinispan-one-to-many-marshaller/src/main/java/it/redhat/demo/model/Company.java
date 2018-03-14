package it.redhat.demo.model;

import java.util.Objects;

import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

@ProtoMessage(name = "Company")
public class Company {

	String name;
	String country;
	String vat;

	public Company() {
	}

	public Company(String name, String country, String vat) {
		this.name = name;
		this.country = country;
		this.vat = vat;
	}

	@ProtoField(number = 1, required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ProtoField(number = 2, required = true)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@ProtoField(number = 3, required = true)
	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Company company = (Company) o;
		return Objects.equals( name, company.name ) &&
				Objects.equals( country, company.country ) &&
				Objects.equals( vat, company.vat );
	}

	@Override
	public int hashCode() {

		return Objects.hash( name, country, vat );
	}

	@Override
	public String toString() {
		return "Company{" +
				"name='" + name + '\'' +
				", country='" + country + '\'' +
				", vat='" + vat + '\'' +
				'}';
	}

}
