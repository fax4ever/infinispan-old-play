package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

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
		writer.writeInt( "code", employee.code );
		writer.writeString( "name", employee.name );
		writer.writeString( "surname", employee.surname );
		writer.writeString( "company", employee.company );
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
