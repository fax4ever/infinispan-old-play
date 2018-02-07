package it.redhat.demo.it;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

/**
 * @author Fabio Massimo Ercoli
 */
@RunWith(Arquillian.class)
public class ExecuteTaskIT {

	@Deployment(order = 0)
	public static WebArchive getJBossDeployment() {

		File file = Maven.resolver()
				.resolve( "it.redhat.demo:task-client:war:1.0-SNAPSHOT" )
				.withoutTransitivity().asSingleFile();

		return ShrinkWrap.createFromZipFile( WebArchive.class, file );

	}

	@Test
	public void test() {

	}

}
