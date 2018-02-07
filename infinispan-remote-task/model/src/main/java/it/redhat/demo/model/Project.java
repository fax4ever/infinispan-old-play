/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.model;

import java.io.Serializable;

/**
 * @author Fabio Massimo Ercoli
 */
public class Project implements Serializable {

	private Integer code;
	private String name;
	private String description;



}
