package it.redhat.demo.service;

import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
public interface ProjectService {

	Project take(String name);

	Project create(String name);

	Project update(String name);

}
