package it.redhat.demo.it;
import static org.junit.Assert.assertEquals;

import java.io.File;
import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.infinispan.client.hotrod.RemoteCache;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import it.redhat.demo.model.Company;
import it.redhat.demo.model.Employee;
import it.redhat.demo.model.GenericEntity;

@RunWith(Arquillian.class)
public class UnmarshallIT {

	@Deployment
	public static WebArchive create() {
		return ShrinkWrap
			.create(WebArchive.class, "one-to-many-marshaller.war")
			.addPackages(true, "it.redhat.demo")
			.addAsWebInfResource( new File( "src/main/webapp/WEB-INF/jboss-deployment-structure.xml") );
	}

	static final Company RED_CORP = new Company( "Red Corp", "US", "red123456789" );
	static final Company GREEN_CORP = new Company( "Green Corp", "US", "green123456789" );

	static final Employee FABIO = new Employee( 731, "Fabio Massimo", "Ercoli", RED_CORP.getName() );
	static final Employee SANNE = new Employee( 777, "Sanne", "Grinovero", RED_CORP.getName() );
	static final Employee DAVIDE = new Employee( 733, "Davide", "D'Alto", RED_CORP.getName() );
	static final Employee MARINELLI = new Employee( 137, "Fabio", "Marinelli", GREEN_CORP.getName() );

	static final GenericEntity RED_CORP_GENERIC = new GenericEntity()
		.put( "name", "Red Corp" ).put( "country", "US" ).put( "vat", "red123456789" );
	static final GenericEntity GREEN_CORP_GENERIC = new GenericEntity()
		.put( "name", "Green Corp" ).put( "country", "US" ).put( "vat", "green123456789" );

	static final GenericEntity FABIO_GENERIC = new GenericEntity()
		.put( "code", 731 ).put( "name", "Fabio Massimo" ).put( "surname", "Ercoli" ).put( "company", RED_CORP.getName() );
	static final GenericEntity SANNE_GENERIC = new GenericEntity()
		.put( "code", 777 ).put( "name", "Sanne" ).put( "surname", "Grinovero" ).put( "company", RED_CORP.getName() );
	static final GenericEntity DAVIDE_GENERIC = new GenericEntity()
		.put( "code", 733 ).put( "name", "Davide" ).put( "surname", "D'Alto" ).put( "company", RED_CORP.getName() );
	static final GenericEntity MARINELLI_GENERIC = new GenericEntity()
		.put( "code", 137 ).put( "name", "Fabio" ).put( "surname", "Marinelli" ).put( "company", GREEN_CORP.getName() );

	@Inject
	private RemoteCache<Integer, Employee> employeeCache;

	@Inject
	private RemoteCache<String, Company> companyCache;

	@Inject
	private RemoteCache<Integer, GenericEntity> employeeCacheGeneric;

	@Inject
	private RemoteCache<String, GenericEntity> companyCacheGeneric;

	@Before
	public void fillData() {
		employeeCache.put( FABIO.getCode(), FABIO );
		employeeCache.put( SANNE.getCode(), SANNE );
		employeeCache.put( DAVIDE.getCode(), DAVIDE );
		employeeCache.put( MARINELLI.getCode(), MARINELLI );

		companyCache.put( RED_CORP.getName(), RED_CORP );
		companyCache.put( GREEN_CORP.getName(), GREEN_CORP );
	}

	@After
	public void cleanUp() {
		employeeCache.clear();
		companyCache.clear();
	}

	@Test
	public void test_unmarshallInClassicWay() {
		assertEquals( 4, employeeCache.size() );
		assertEquals( 2, companyCache.size() );

		assertEquals( FABIO, employeeCache.get( FABIO.getCode() ) );
		assertEquals( SANNE, employeeCache.get( SANNE.getCode() ) );
		assertEquals( DAVIDE, employeeCache.get( DAVIDE.getCode() ) );
		assertEquals( MARINELLI, employeeCache.get( MARINELLI.getCode() ) );

		assertEquals( RED_CORP, companyCache.get( RED_CORP.getName() ) );
		assertEquals( GREEN_CORP, companyCache.get( GREEN_CORP.getName() ) );
	}

	@Test
	public void test_unmarshallIntoGenericEntity() {
		assertEquals( 4, employeeCacheGeneric.size() );
		assertEquals( 2, companyCacheGeneric.size() );

		assertEquals( FABIO_GENERIC, employeeCacheGeneric.get( FABIO.getCode() ) );
		assertEquals( SANNE_GENERIC, employeeCacheGeneric.get( SANNE.getCode() ) );
		assertEquals( DAVIDE_GENERIC, employeeCacheGeneric.get( DAVIDE.getCode() ) );
		assertEquals( MARINELLI_GENERIC, employeeCacheGeneric.get( MARINELLI.getCode() ) );

		assertEquals( RED_CORP_GENERIC, companyCacheGeneric.get( RED_CORP.getName() ) );
		assertEquals( GREEN_CORP_GENERIC, companyCacheGeneric.get( GREEN_CORP.getName() ) );
	}

}
