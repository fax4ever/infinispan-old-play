package it.redhat.demo.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
public class IntegrationMarshallerIT {

	@Deployment
	public static WebArchive create() {
		return ShrinkWrap
			.create(WebArchive.class, "one-to-many-marshaller.war")
			.addPackages(true, "it.redhat.demo")
			.addAsWebInfResource( new File( "src/main/webapp/WEB-INF/jboss-deployment-structure.xml") );
	}

	private Company redCorp = new Company( "Red Corp", "US", "red123456789" );
	private Company greenCorp = new Company( "Green Corp", "US", "green123456789" );

	private Employee fabio = new Employee( 731, "Fabio Massimo", "Ercoli", redCorp.getName() );
	private Employee sanne = new Employee( 777, "Sanne", "Grinovero", redCorp.getName() );
	private Employee davide = new Employee( 733, "Davide", "D'Alto", redCorp.getName() );
	private Employee marinelli = new Employee( 137, "Fabio", "Marinelli", greenCorp.getName() );

	private GenericEntity redCorpG = new GenericEntity().put( "name", "Red Corp" ).put( "country", "US" ).put( "vat", "red123456789" );
	private GenericEntity greenCorpG = new GenericEntity().put( "name", "Green Corp" ).put( "country", "US" ).put( "vat", "green123456789" );

	private GenericEntity fabioG = new GenericEntity().put( "code", 731 ).put( "name", "Fabio Massimo" )
			.put( "surname", "Ercoli" ).put( "company", "Red Corp" );
	private GenericEntity sanneG = new GenericEntity().put( "code", 777 ).put( "name", "Sanne" )
			.put( "surname", "Grinovero" ).put( "company", "Red Corp" );
	private GenericEntity davideG = new GenericEntity().put( "code", 733 ).put( "name", "Davide" )
			.put( "surname", "D'Alto" ).put( "company", "Red Corp" );
	private GenericEntity marinelliG = new GenericEntity().put( "code", 137 ).put( "name", "Fabio" )
			.put( "surname", "Marinelli" ).put( "company", "Green Corp" );

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
		employeeCache.put( fabio.getCode(), fabio );
		employeeCache.put( sanne.getCode(), sanne );
		employeeCache.put( davide.getCode(), davide );
		employeeCache.put( marinelli.getCode(), marinelli );

		companyCache.put( redCorp.getName(), redCorp );
		companyCache.put( greenCorp.getName(), greenCorp );
	}

	@After
	public void cleanUp() {
		employeeCache.clear();
		companyCache.clear();
	}

	@Test
	public void test() {
		assertEquals( 4, employeeCacheGeneric.size() );
		assertEquals( 2, companyCacheGeneric.size() );

		assertEquals( fabioG, employeeCacheGeneric.get( fabio.getCode() ) );
		assertEquals( sanneG, employeeCacheGeneric.get( sanne.getCode() ) );
		assertEquals( davideG, employeeCacheGeneric.get( davide.getCode() ) );
		assertEquals( marinelliG, employeeCacheGeneric.get( marinelli.getCode() ) );
	}

}
