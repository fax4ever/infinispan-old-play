package it.redhat.demo.service;


import static org.junit.Assert.assertEquals;

import java.io.File;
import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.infinispan.counter.api.StrongCounter;
import org.infinispan.counter.api.WeakCounter;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

@RunWith(Arquillian.class)
public class CounterServiceIT {

	@Deployment
	public static WebArchive create() {
		return ShrinkWrap
				.create( WebArchive.class, "infinispan-jboss-demo.war" )
				.addPackages( true, "it.redhat.demo" )
				.addAsWebInfResource( new File( "src/main/webapp/WEB-INF/jboss-deployment-structure.xml" ) );
	}

	@Inject
	private WeakCounter weakCounter;

	@Inject
	private StrongCounter strongCounter;

	@Test
	public void test_weakCounter() throws Exception {
		weakCounter.increment();
		assertEquals ( 1, weakCounter.getValue() );

		weakCounter.increment();
		assertEquals ( 2, weakCounter.getValue() );

		weakCounter.increment();
		assertEquals ( 3, weakCounter.getValue() );
	}

	@Test
	public void test_strongCounter() throws Exception {
		assertEquals ( new Long( 1 ), strongCounter.incrementAndGet().get() );
		assertEquals ( new Long( 2 ), strongCounter.incrementAndGet().get() );
		assertEquals ( new Long( 3 ), strongCounter.incrementAndGet().get() );
	}
}
