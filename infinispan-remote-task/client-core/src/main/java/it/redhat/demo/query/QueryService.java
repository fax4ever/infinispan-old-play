package it.redhat.demo.query;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import it.redhat.demo.cache.ProtoStream;
import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
@Stateless
public class QueryService {

	@Inject
	@ProtoStream
	private RemoteCache<String, Project> protoCache;

	public List<Project> findByCode( Integer code ) {

		QueryFactory qf = Search.getQueryFactory( protoCache );
		Query query = qf.from( Project.class )
				.having( "code" ).equal( code )
			.and()
				.having( "name" ).like( "J%" )
			.build();

		return query.list();

	}

	public List<Project> findByName( String name ) {

		QueryFactory qf = Search.getQueryFactory( protoCache );
		Query query = qf.from( Project.class )
			.having( "name" ).equal( name )
			.build();

		return query.list();

	}

	public List<Project> findByDescription( String desription ) {

		QueryFactory qf = Search.getQueryFactory( protoCache );
		Query query = qf.from( Project.class )
				.having( "desription" ).equal( desription )
				.build();

		return query.list();

	}

}
