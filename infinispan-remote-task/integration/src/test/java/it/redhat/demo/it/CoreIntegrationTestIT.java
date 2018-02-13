package it.redhat.demo.it;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * @author Fabio Massimo Ercoli
 */
@RunWith(Arquillian.class)
public class CoreIntegrationTestIT {

	@Deployment(name = "jboss")
	@TargetsContainer("jboss")
	public static WebArchive getJBossDeployment() {

		WebArchive javaArchive = ShrinkWrap.create( WebArchive.class )
			.addPackages( true, "it.redhat.demo" )
			.addAsWebInfResource( "jboss-deployment-structure.xml" );

		return javaArchive;

	}

	@Deployment(name = "infinispan", testable = false)
	@TargetsContainer("infinispan")
	public static JavaArchive getInfinispanDeployment() {

		File file = Maven.resolver()
				.resolve( "it.redhat.demo:task-server:jar:1.0-SNAPSHOT" )
				.withoutTransitivity().asSingleFile();

		return ShrinkWrap.createFromZipFile( JavaArchive.class, file );

	}

	@Test
	@OperateOnDeployment("jboss")
	public void test() {

	}


}
