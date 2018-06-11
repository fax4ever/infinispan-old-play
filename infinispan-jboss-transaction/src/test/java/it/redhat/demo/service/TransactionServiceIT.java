package it.redhat.demo.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

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
				.create( WebArchive.class, "infinispan-jboss-demo.war" )
				.addPackages( true, "it.redhat.demo" )
				.addAsWebInfResource( new File( "src/main/webapp/WEB-INF/jboss-deployment-structure.xml" ) );
	}

	@Inject
	private UserTransaction utx;

	@Inject
	private RemoteCache<Integer, Puzzle> puzzleCache;

	//@Test
	//TODO: Transactions should work
	public void test_withTransactions() throws Exception {
		utx.begin();
		puzzleCache.put( 1, new Puzzle( "ciao1" ) );
		utx.commit();

		utx.begin();
		puzzleCache.put( 2, new Puzzle( "ciao2" ) );

		Puzzle puzzle = puzzleCache.get( 2 );
		assertNotNull( puzzle );

		utx.rollback();

		puzzle = puzzleCache.get( 2 );
		assertNull( puzzle );
	}

	@Test
	public void test_noTransactions() {
		puzzleCache.put( 1, new Puzzle( "ciao1" ) );
		puzzleCache.put( 2, new Puzzle( "ciao2" ) );

		assertNotNull( puzzleCache.get( 1 ) );
		assertNotNull( puzzleCache.get( 2 ) );
	}
}
