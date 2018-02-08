package it.redhat.demo.it;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * @author Fabio Massimo Ercoli
 */
@RunWith(Arquillian.class)
public class ExecuteTaskIT {

	@Deployment(name = "jboss") @TargetsContainer("jboss")
	public static WebArchive getJBossDeployment() {

		File file = Maven.resolver()
				.resolve( "it.redhat.demo:task-client:war:1.0-SNAPSHOT" )
				.withoutTransitivity().asSingleFile();

		return ShrinkWrap.createFromZipFile( WebArchive.class, file );

	}

	@Deployment(name = "infinispan", testable = false) @TargetsContainer("infinispan")
	public static JavaArchive getInfinispanDeployment() {

		File file = Maven.resolver()
				.resolve( "it.redhat.demo:task-server:jar:1.0-SNAPSHOT" )
				.withoutTransitivity().asSingleFile();

		return ShrinkWrap.createFromZipFile( JavaArchive.class, file );

	}

	@ArquillianResource
	@OperateOnDeployment( "jboss" )
	private URL deploymentURL;

	@Test
	@RunAsClient
	@InSequence(1)
	public void test_ciaoRemoteTask() {

		String response = ClientBuilder.newClient()
			.target( deploymentURL.toString() )
			.path( "task" )
			.path( "ciao" )
			.request().get( String.class );

		assertEquals("ciao", response);

	}

	@Test
	@RunAsClient
	@InSequence(2)
	public void test_insertProject() {

		String name = ClientBuilder.newClient()
			.target( deploymentURL.toString() )
			.path( "cache" )
			.path( "project" )
			.path( "HibernateOGM" )
			.request().post( Entity.text( "" ), String.class );

		assertEquals("HibernateOGM", name);

	}

}
