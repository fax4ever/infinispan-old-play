package it.redhat.demo.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.demo.cache.CacheManagerFactory;
import it.redhat.demo.cache.CacheRepo;
import it.redhat.demo.model.Message;

public class FullTextQueryIT {

	private static final Logger LOG = LoggerFactory.getLogger( FullTextQueryIT.class );

	private static final Message[] MESSAGES = {

			// Ah Sunflower - Poem by William Blake - 1794
			new Message( 1l, "wblake", "I was angry with my #friend: I told my wrath, my wrath did end. I was angry with my foe: I told it not, my wrath did grow." ),
			new Message( 2l, "wblake", "And I watered it in fears, Night and morning with my #tears; And I sunned it with smiles, And with soft deceitful wiles." ),
			new Message( 3l, "wblake", "And it grew both day and night, Till it bore an apple bright. And my foe beheld it shine. And he knew that it was mine," ),
			new Message( 4l, "wblake", "And into my garden stole When the night had veiled the pole; In the morning glad I seeMy foe outstretched beneath the tree. " ),

			// A song - Poem by William Blake - 1789
			new Message( 5l,"wblake", "Sweet dreams, form a shade O'er my lovely infant's head! Sweet dreams of pleasant streams By happy, silent, moony beams!" ),
			new Message( 6l,"wblake", "Sweet Sleep, with soft down Weave thy brows an infant crown Sweet Sleep, angel mild, Hover o'er my happy child!" ),
			new Message( 7l,"wblake", "Sweet smiles, in the night Hover over my delight! Sweet smiles, mother's smile, All the livelong night beguile." ),
			new Message( 8l,"wblake", "Sweet moans, dovelike sighs, Chase not slumber from thine eyes! Sweet moan, sweeter smile, All the dovelike moans beguile." ),
			new Message( 9l,"wblake", "Sleep, sleep, happy child! All creation slept and smiled. Sleep, sleep, happy sleep, While o'er thee doth mother weep." ),
			new Message( 10l,"wblake", "Sweet babe, in thy face Holy image I can trace; Sweet babe, once like thee Thy Maker lay, and wept for me:" ),
			new Message( 11l,"wblake", "Wept for me, for thee, for all, When He was an infant small. Thou His image ever see, Heavenly face that smiles on thee!" ),
			new Message( 12l,"wblake", "Smiles on thee, on me, on all, Who became an infant small; Infant smiles are his own smiles; Heaven and earth to peace beguiles." )
	};

	RemoteCacheManager cacheManager;
	CacheRepo cacheRepo;
	RemoteCache<Long, Message> messageCache;

	@Before
	public void before() {
		cacheManager = new CacheManagerFactory().create();
		cacheRepo = new CacheRepo( cacheManager );
		cacheRepo.create();
		messageCache = cacheRepo.getMessageCache();

		for (Message message : MESSAGES) {
			messageCache.put( message.getId(), message );
		}
	}

	@After
	public void after() throws Exception {
		cacheRepo.dispose();
		cacheManager.close();
		cacheManager = null;
		cacheRepo = null;
		messageCache = null;
	}

	@Test
	public void findByTerm() {
		QueryFactory qf = Search.getQueryFactory( messageCache );
		Query query = qf.create( "from ProtoModel.Message where body:'Sweet'" );
		List<Message> list = query.list();
		assertEquals(5, list.size());
	}

}
