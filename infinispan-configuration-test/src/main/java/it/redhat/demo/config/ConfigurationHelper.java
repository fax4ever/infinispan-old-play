package it.redhat.demo.config;

import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ParserRegistry;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.EmbeddedTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;

public class ConfigurationHelper {

	private static final String SINGLE_CACHE_XML_CONFIG =
		"<infinispan><cache-container>" +
			"	<distributed-cache-configuration name=\"my-cache\">" +
			"     <locking isolation=\"REPEATABLE_READ\"/>" +
			"     <transaction locking=\"PESSIMISTIC\" mode=\"NON_DURABLE_XA\" />" +
			"   </distributed-cache-configuration>" +
			"</cache-container></infinispan>";

	public static Configuration configurationUsedByInfinispanInHotrodTrxTest() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.clustering().cacheMode(CacheMode.DIST_SYNC);
		builder.locking().isolationLevel(IsolationLevel.REPEATABLE_READ);
		builder.transaction().lockingMode(LockingMode.PESSIMISTIC);
		builder.transaction().transactionMode(TransactionMode.TRANSACTIONAL);
		builder.transaction().transactionManagerLookup(new EmbeddedTransactionManagerLookup());
		builder.transaction().cacheStopTimeout(0L);
		return builder.build();
	}

	//xml single element is used by HotRod admin api to configure new caches
	public static Configuration configurationFromXmlSingleElement() {
		ParserRegistry parserRegistry = new ParserRegistry();
		ConfigurationBuilderHolder holder = parserRegistry.parse( SINGLE_CACHE_XML_CONFIG );
		return holder.getCurrentConfigurationBuilder().build();
	}

	//full xml is used in embedded | library mode
	public static Configuration configurationFromFullXml() {
		ParserRegistry parserRegistry = new ParserRegistry();
		ConfigurationBuilderHolder holder = parserRegistry.parse( ConfigurationHelper.class.getClassLoader().getResourceAsStream( "infinispan.xml" ) );
		return holder.getCurrentConfigurationBuilder().build();
	}
}
