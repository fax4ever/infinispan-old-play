package it.redhat.demo.service;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.infinispan.client.hotrod.RemoteCache;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import it.redhat.demo.model.Puzzle;

@RunWith(Arquillian.class)
public class TransactionServiceIT {

	@Deployment
	public static WebArchive create() {
		return ShrinkWrap
			.create(WebArchive.class, "infinispan-jboss-demo.war")
			.addPackages(true, "it.redhat.demo")
			.addAsWebInfResource( new File( "src/main/webapp/WEB-INF/jboss-deployment-structure.xml") );
	}

	@Inject
	private RemoteCache<Integer, Puzzle> puzzleCache;

	@Test
	public void test() {
		puzzleCache.put( 1, new Puzzle( "ciao" ) );
		Puzzle puzzle = puzzleCache.get( 1 );

		assertEquals("ciao", puzzle.getName());
	}
}
