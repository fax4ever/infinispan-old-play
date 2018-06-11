package it.redhat.demo.service;

import static org.junit.Assert.assertNotNull;

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
	private RemoteCache<String, String> puzzleCache;

	//@Test
	//TODO: Transactions should work
	public void test_withTransactions() throws Exception {
		utx.begin();
		puzzleCache.put( "ciao", "ciao" );
		utx.commit();

		assertNotNull( puzzleCache.get( "ciao" ) );
	}

	@Test
	public void test_noTransactions() throws Exception {
		puzzleCache.put( "ciao", "ciao" );
		assertNotNull( puzzleCache.get( "ciao" ) );
	}
}
