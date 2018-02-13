package it.redhat.demo.it;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@RunWith(Arquillian.class)
public class ExecuteTaskIT {

	@Deployment(name = "jboss")
	@TargetsContainer("jboss")
	public static WebArchive getJBossDeployment() {

		File file = Maven.resolver()
				.resolve( "it.redhat.demo:task-client:war:1.0-SNAPSHOT" )
				.withoutTransitivity().asSingleFile();

		return ShrinkWrap.createFromZipFile( WebArchive.class, file );

	}

	@Deployment(name = "infinispan", testable = false)
	@TargetsContainer("infinispan")
	public static JavaArchive getInfinispanDeployment() {

		File file = Maven.resolver()
				.resolve( "it.redhat.demo:task-server:jar:1.0-SNAPSHOT" )
				.withoutTransitivity().asSingleFile();

		return ShrinkWrap.createFromZipFile( JavaArchive.class, file );

	}

	@ArquillianResource
	@OperateOnDeployment("jboss")
	private URL deploymentURL;

	private Client client;

	@Before
	public void initClient() {
		client = ClientBuilder.newClient();
	}

	@After
	public void cleanUpClient() {
		client.close();
	}

	@Test
	@RunAsClient
	public void test_ciaoRemoteTask() {

		String response = client
			.target( deploymentURL.toString() )
			.path( "task" )
			.path( "ciao" )
			.request().get( String.class );

		assertEquals( "ciao", response );

	}

	@Test
	@RunAsClient
	public void test_usingCache() {

		String projectName = "HibernateOGM";

		WebTarget path = client
			.target( deploymentURL.toString() )
			.path( "cache" )
			.path( "project" )
			.path( projectName );

		Project project = path.request()
			.post( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(1), project.getCode() );

		project = path.request()
			.put( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

		project = path.request()
			.get( Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

	}

	@Test
	@RunAsClient
	public void test_usingTask() {

		String projectName = "HibernateSearch";

		WebTarget path = client
			.target( deploymentURL.toString() )
			.path( "task" )
			.path( "project" )
			.path( projectName );

		Project project = path.request()
			.post( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(1), project.getCode() );

		project = path.request()
			.put( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

		project = path.request()
			.get( Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

	}

	// At the time of writing compatibility mode seems not working
	/*@Test
	@RunAsClient*/
	public void test_compatibility_mode_CTC() {

		String projectName = "compatibityKeyCTC";

		WebTarget cachePath = client
			.target( deploymentURL.toString() )
			.path( "cache" )
			.path( "project" )
			.path( projectName );

		WebTarget taskPath = client
			.target( deploymentURL.toString() )
			.path( "task" )
			.path( "project" )
			.path( projectName );

		Project project = cachePath.request()
			.post( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(1), project.getCode() );

		project = taskPath.request()
			.put( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

		project = cachePath.request()
			.get( Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

	}

	// At the time of writing compatibility mode seems not working
	/*@Test
	@RunAsClient*/
	public void test_compatibility_mode_TCT() {

		String projectName = "compatibityKeyTCT";

		WebTarget cachePath = client
				.target( deploymentURL.toString() )
				.path( "cache" )
				.path( "project" )
				.path( projectName );

		WebTarget taskPath = client
				.target( deploymentURL.toString() )
				.path( "task" )
				.path( "project" )
				.path( projectName );

		Project project = taskPath.request()
				.post( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(1), project.getCode() );

		project = cachePath.request()
				.put( Entity.text( "" ), Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

		project = taskPath.request()
				.get( Project.class );

		assertEquals( projectName, project.getName() );
		assertEquals( new Integer(2), project.getCode() );

	}


}
