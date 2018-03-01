package it.redhat.demo.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import org.infinispan.protostream.MessageMarshaller;
import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

/**
 * @author Fabio Massimo Ercoli
 */

@ProtoMessage(name = "Project")
@ProtoDoc("@Indexed")
public class Project implements Serializable {

	private Integer code;

	private String name;

	private String description;

	public Project() {
	}

	public Project(Integer code, String name, String description) {
		this.code = code;
		this.name = name;
		this.description = description;
	}

	@ProtoDoc("@IndexedField(index = true, store = false)")
	@ProtoField(number = 1, required = true)
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@ProtoDoc("@IndexedField(index = true, store = false)")
	@ProtoField(number = 2, required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ProtoDoc("@IndexedField(index = false, store = false)")
	@ProtoField(number = 3, required = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		Project project = (Project) o;
		return Objects.equals( code, project.code ) &&
				Objects.equals( name, project.name ) &&
				Objects.equals( description, project.description );
	}

	@Override
	public int hashCode() {
		return Objects.hash( code, name, description );
	}

	@Override
	public String toString() {
		return "Project{" +
				"code=" + code +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}

	public static final class Marshaller implements MessageMarshaller<Project> {

		@Override
		public Project readFrom(ProtoStreamReader reader) throws IOException {

			Integer code = reader.readInt( "code" );
			String name = reader.readString( "name" );
			String description = reader.readString( "description" );

			return new Project( code, name, description );
		}

		@Override
		public void writeTo(ProtoStreamWriter writer, Project project) throws IOException {

			writer.writeInt( "code", project.code );
			writer.writeString( "name", project.name );
			writer.writeString( "description", project.description );

		}

		@Override
		public Class<? extends Project> getJavaClass() {
			return Project.class;
		}

		@Override
		public String getTypeName() {
			return "it.redhat.demo.model.Project";
		}

	}

}
