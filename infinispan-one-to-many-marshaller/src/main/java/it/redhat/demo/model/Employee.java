package it.redhat.demo.model;

import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

@ProtoMessage(name = "Employee")
public class Employee {

	Integer code;
	String name;
	String surname;
	String company;

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
}
