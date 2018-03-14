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

@RunWith(Arquillian.class)
public class OneToManyMarshallerIT {

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

	@Inject
	private RemoteCache<Integer, Employee> employeeCache;

	@Inject
	private RemoteCache<String, Company> companyCache;

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

		assertEquals( 4, employeeCache.size() );
		assertEquals( 2, companyCache.size() );

		assertEquals( fabio, employeeCache.get( fabio.getCode() ) );
		assertEquals( sanne, employeeCache.get( sanne.getCode() ) );
		assertEquals( davide, employeeCache.get( davide.getCode() ) );
		assertEquals( marinelli, employeeCache.get( marinelli.getCode() ) );

		assertEquals( redCorp, companyCache.get( redCorp.getName() ) );
		assertEquals( greenCorp, companyCache.get( greenCorp.getName() ) );

	}

}
