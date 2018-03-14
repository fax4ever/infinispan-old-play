package it.redhat.demo.model;

import java.util.Objects;

import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

@ProtoMessage(name = "Employee")
public class Employee {

	private Integer code;
	private String name;
	private String surname;
	private String company;

	public Employee() {
	}

	public Employee(Integer code, String name, String surname, String company) {
		this.code = code;
		this.name = name;
		this.surname = surname;
		this.company = company;
	}

	@ProtoField(number = 1, required = true)
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@ProtoField(number = 2, required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ProtoField(number = 3, required = true)
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@ProtoField(number = 4, required = true)
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Employee employee = (Employee) o;
		return Objects.equals( code, employee.code ) &&
				Objects.equals( name, employee.name ) &&
				Objects.equals( surname, employee.surname ) &&
				Objects.equals( company, employee.company );
	}

	@Override
	public int hashCode() {

		return Objects.hash( code, name, surname, company );
	}

	@Override
	public String toString() {
		return "Employee{" +
				"code=" + code +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", company='" + company + '\'' +
				'}';
	}

}
