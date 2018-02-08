/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.model;

import java.io.Serializable;
import java.util.Objects;

import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoMessage;

/**
 * @author Fabio Massimo Ercoli
 */

@ProtoMessage(name = "Project")
public class Project implements Serializable {

	private Integer code;

	private String name;

	private String description;

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

}
