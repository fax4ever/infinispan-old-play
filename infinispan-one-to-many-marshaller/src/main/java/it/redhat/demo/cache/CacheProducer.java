package it.redhat.demo.cache;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;

import org.slf4j.Logger;

import it.redhat.demo.model.Company;
import it.redhat.demo.model.Employee;

/**
 * @author Fabio Massimo Ercoli
 */
@Singleton
@Startup
public class CacheProducer {

	public static final String EMPLOYEE_CACHE = "employee";
	public static final String COMPANY_CACHE = "company";
	@Inject
	private Logger log;

	private boolean updateServer = false;

	@Inject
	private RemoteCacheManager cacheManager;

	@PostConstruct
	private void onStartup() {
		cacheManager.start();

		cacheManager.administration().getOrCreateCache( EMPLOYEE_CACHE, (String) null );
		cacheManager.administration().getOrCreateCache( COMPANY_CACHE, (String) null );
	}

	@PreDestroy
	private void onShutdown() {
		cacheManager.stop();
	}

	@Produces
	public RemoteCache<Integer, Employee> getEmployeeCache() {
		return cacheManager.<Integer, Employee>getCache( EMPLOYEE_CACHE);
	}

	@Produces
	public RemoteCache<String, Company> getProtoCache() {
		return cacheManager.<String, Company>getCache( COMPANY_CACHE );
	}

}
