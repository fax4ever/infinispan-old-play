import static org.fest.assertions.Assertions.assertThat;

import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

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

	//@Test
	//TODO: Error on commit() :(
	public void test_withTransactions() throws Exception {
		RemoteCache<String, String> cache = cacheRepo.getCache( "my-cache-name" );

		TransactionManager userTrx = cache.getTransactionManager();
		userTrx.begin();
		cache.put( "Greetings", "Hi!" );

//		Jun 11, 2018 2:03:28 PM org.infinispan.client.hotrod.impl.protocol.Codec20 checkForErrorsInResponseStatus
//		WARN: ISPN004005: Error received from the server: java.lang.IllegalArgumentException: Unable to decorate cache
//		Jun 11, 2018 2:03:28 PM org.infinispan.client.hotrod.impl.transaction.TransactionContext prepareContext
//		WARN: ISPN004086: Exception caught while preparing transaction Xid{formatId=1213355096, globalTransactionId=B601E26ED84D4C25139634C4D13346E000000163EEBAC9B00000000000000001,branchQualifier=B601E26ED84D4C25139634C4D13346E000000163EEBAC9B00000000000000001}
//		java.util.concurrent.ExecutionException: org.infinispan.client.hotrod.exceptions.HotRodClientException:Request for messageId=6 returned server error (status=0x85): java.lang.IllegalArgumentException: Unable to decorate cache
		userTrx.commit();

		userTrx.begin();
		assertThat( cache.get( "Greetings" ) ).isEqualTo( "Hi!" );
		userTrx.commit();
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
}
