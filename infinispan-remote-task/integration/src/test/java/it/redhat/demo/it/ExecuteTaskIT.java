package it.redhat.demo.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

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

	@Test
	@RunAsClient
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

	@Test
	@RunAsClient
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

	@Test
	@RunAsClient
	public void test_query_compatibility() {

		WebTarget cachePath = client
				.target( deploymentURL.toString() )
				.path( "cache" )
				.path( "project" );

		WebTarget taskPath = client
				.target( deploymentURL.toString() )
				.path( "task" )
				.path( "project" );

		// create Jboss1 project using cache
		Project jBoss1 = cachePath.path( "JBoss1" )
			.request()
			.post( Entity.text( "" ), Project.class );

		assertEquals( "JBoss1", jBoss1.getName() );
		assertEquals( new Integer(1), jBoss1.getCode() );

		// create Jboss2 project using task
		Project jBoss2 = taskPath.path( "JBoss2" )
			.request()
			.post( Entity.text( "" ), Project.class );

		assertEquals( "JBoss2", jBoss2.getName() );
		assertEquals( new Integer(1), jBoss2.getCode() );

		// query the server
		List<Project> projects = cachePath.path( "code" ).path( "1" )
			.request()
			.get( new GenericType<List<Project>>() {} );

		assertEquals( 2, projects.size() );

		// collect names
		Set<String> collect = projects.stream()
				.map( project -> project.getName() )
				.collect( Collectors.toSet() );

		assertTrue( collect.contains( "JBoss1" ));
		assertTrue( collect.contains( "JBoss2" ));

		// using task for update Jboss1 project
		jBoss1 = taskPath.path( "JBoss1" )
			.request()
			.put( Entity.text( "" ), Project.class );

		assertEquals( "JBoss1", jBoss1.getName() );
		assertEquals( new Integer(2), jBoss1.getCode() );

		// query for code 1
		projects = cachePath.path( "code" ).path( "1" )
				.request()
				.get( new GenericType<List<Project>>() {} );

		assertEquals( 1, projects.size() );
		assertEquals( "JBoss2", projects.get( 0 ).getName() );
		assertEquals( new Integer(1), projects.get( 0 ).getCode() );

	}

}
