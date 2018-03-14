package it.redhat.demo.cache;

import java.io.IOException;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheCreationIT {

	private static final Logger LOG = LoggerFactory.getLogger( CacheCreationIT.class );

	public static final String DEFAULT = "default";
	public static final String REPL = "repl";
	public static final String NAMED_CACHE = "namedCache";

	private static CacheManagerService service;

	@BeforeClass
	public static void beforeClass() {

		service = new CacheManagerService();

	}

	@AfterClass
	public static void afterClass() throws IOException {

		service.close();

	}

	@Test
	public void testCaches() {

		service.testCacheExists( DEFAULT );
		service.testCacheExists( REPL );
		service.testCacheExists( NAMED_CACHE );
		service.testCacheExists( "trx" );

	}

	@Test
	public void testCreation() {

		String baseName = UUID.randomUUID().toString();

		service.createCacheFrom( baseName, null );
		service.createCacheFrom( baseName, "replicated" );
		service.createCacheFrom( baseName, "transactional" );
		service.createCacheFrom( baseName, "async" );
		service.createCacheFrom( baseName, "indexed" );
		service.createCacheFrom( baseName, "default" );
		service.createCacheFrom( baseName, "trx" );
		service.createCacheFrom( baseName, "rpl" );

	}


}
