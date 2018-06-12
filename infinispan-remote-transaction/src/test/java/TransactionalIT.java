import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;

import it.redhat.demo.cache.CacheFactory;
import it.redhat.demo.cache.CacheManagerFactory;

public class TransactionalIT {

	CacheFactory cacheRepo;

	@Before
	public void before() {
		RemoteCacheManager cacheManager = new CacheManagerFactory().create();
		cacheRepo = new CacheFactory( cacheManager );
	}

	@After
	public void after() throws Exception {
		cacheRepo.close();
	}

	@Test
	public void test_noTransactions() {
		RemoteCache<String, String> cache = cacheRepo.getCache( "my-cache-name" );
		cache.put( "Greetings", "Hi!" );
		assertThat( cache.get( "Greetings" ) ).isEqualTo( "Hi!" );
	}

	@Test
	public void test_withTransactions() throws Exception {
		RemoteCache<String, String> cache = cacheRepo.getCache( "my-cache-name" );

		TransactionManager userTrx = cache.getTransactionManager();
		userTrx.begin();
		cache.put( "Greetings", "Hi!" );
		userTrx.commit();

		userTrx.begin();
		assertThat( cache.get( "Greetings" ) ).isEqualTo( "Hi!" );
		userTrx.commit();
	}

	@Test
	public void test_rollback() throws Exception {
		RemoteCache<String, String> cache = cacheRepo.getCache( "just-another-cache" );

		TransactionManager userTrx = cache.getTransactionManager();

		userTrx.begin();
		cache.put( "Greetings", "Hi!" );
		assertThat( cache.get( "Greetings" ) ).isEqualTo( "Hi!" );
		userTrx.rollback();

		assertThat( cache.get( "Greetings" ) ).isNull();
	}

	//@Test
	//TODO: Error on commit() :(
	public void test_withTransactions_serverSideDefinedCache() throws Exception {
		RemoteCache<String, String> cache = cacheRepo.getCache( CacheFactory.TRANSACTIONAL_SERVER_SIDE_DEFINED_CACHE );

		TransactionManager userTrx = cache.getTransactionManager();
		userTrx.begin();
		cache.put( "Greetings", "Hi!" );

//		WARN: ISPN004005: Error received from the server: javax.transaction.InvalidTransactionException: WFTXN0002: Transaction is not a supported instance: TransactionImpl{xid=Xid{formatId=1213355096, globalTransactionId=ACE72FF3C45B1C95ECDD0CABA4474FBD00000163EF327F9A0000000000000002,branchQualifier=ACE72FF3C45B1C95ECDD0CABA4474FBD00000163EF327F9A0000000000000002}, status=ACTIVE}
//		javax.transaction.RollbackException: Transaction marked as rollback only.
		userTrx.commit();

		userTrx.begin();
		assertThat( cache.get( "Greetings" ) ).isEqualTo( "Hi!" );
		userTrx.commit();
	}

	@Test
	public void test_useNonTransactionalCache() throws Exception {
		RemoteCache<String, String> cache = cacheRepo.getCache( CacheFactory.NON_TRANSACTIONAL_SERVER_SIDE_DEFINED_CACHE );

		TransactionManager userTrx = cache.getTransactionManager();
		userTrx.begin();

		try {
			cache.put( "Greetings", "Hi!" );
			fail( "exception expected" );
		} catch (HotRodClientException hotException) {
			// org.infinispan.client.hotrod.exceptions.HotRodClientException:Request for messageId=4 returned server error (status=0x85): java.lang.IllegalStateException: ISPN006020: Cache 'default' is not transactional to execute a client transaction
			assertThat( hotException.getMessage() ).contains( "ISPN004084" );
		}
	}
}
