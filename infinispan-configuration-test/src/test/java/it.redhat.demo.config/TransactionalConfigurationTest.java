package it.redhat.demo.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.util.concurrent.IsolationLevel;

@RunWith(JUnit4.class)
public class TransactionalConfigurationTest {

	@Test
	public void configurationUsedByInfinispanInHotrodTrxTest() {
		Configuration configuration = ConfigurationHelper.configurationUsedByInfinispanInHotrodTrxTest();
		mustBeTransactional(configuration);
	}

	@Test
	public void configurationFromXmlSingleElement() {
		Configuration configuration = ConfigurationHelper.configurationFromXmlSingleElement();
		mustBeTransactional(configuration);
	}

	@Test
	public void configurationFromFullXml() {
		Configuration configuration = ConfigurationHelper.configurationFromFullXml();
		mustBeTransactional(configuration);
	}

	public void mustBeTransactional(Configuration configuration) {
		assertEquals( IsolationLevel.REPEATABLE_READ, configuration.locking().isolationLevel() );
		assertEquals( LockingMode.PESSIMISTIC, configuration.transaction().lockingMode() );
		assertEquals( TransactionMode.TRANSACTIONAL, configuration.transaction().transactionMode() );
	}
}
