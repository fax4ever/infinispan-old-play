package it.redhat.demo.it;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.infinispan.client.hotrod.RemoteCache;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

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

	@Inject
	private RemoteCache<Integer, Employee> employeeCache;

	@Test
	public void test() {
		assertNotNull( employeeCache );
	}

}
