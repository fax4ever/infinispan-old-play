package it.redhat.demo.cache.specific;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

import it.redhat.demo.model.Employee;

public class EmployeeMarshaller implements MessageMarshaller<Employee> {

	@Override
	public Employee readFrom(ProtoStreamReader reader) throws IOException {
		Integer code = reader.readInt( "code" );
		String name = reader.readString( "name" );
		String surname = reader.readString( "surname" );
		String company = reader.readString( "company" );

		return new Employee( code, name, surname, company );
	}

	@Override
	public void writeTo(ProtoStreamWriter writer, Employee employee) throws IOException {
		writer.writeInt( "code", employee.getCode() );
		writer.writeString( "name", employee.getName() );
		writer.writeString( "surname", employee.getSurname() );
		writer.writeString( "company", employee.getCompany() );
	}

	@Override
	public Class<? extends Employee> getJavaClass() {
		return Employee.class;
	}

	@Override
	public String getTypeName() {
		return Employee.class.getName();
	}

}
