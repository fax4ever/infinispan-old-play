package it.redhat.demo.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Fabio Massimo Ercoli
 */
public class Employee implements Serializable {

	private Integer id;

	private String name;
	private String surname;
	private Date hiringDate;

	private List<Project> projects = new ArrayList<Project>(  );


}
