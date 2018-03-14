package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class CompanyMarshaller implements MessageMarshaller<Company> {

	@Override
	public Company readFrom(ProtoStreamReader reader) throws IOException {
		String name = reader.readString( "name" );
		String country = reader.readString( "country" );
		String vat = reader.readString( "vat" );

		return new Company( name, country, vat );
	}

	@Override
	public void writeTo(ProtoStreamWriter writer, Company company) throws IOException {
		writer.writeString( "name", company.name );
		writer.writeString( "country", company.country );
		writer.writeString( "vat", company.vat );
	}

	@Override
	public Class<? extends Company> getJavaClass() {
		return Company.class;
	}

	@Override
	public String getTypeName() {
		return Company.class.getName();
	}

}
