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

import it.redhat.demo.cache.generic.UseGenericEntityMarshaller;
import it.redhat.demo.cache.listener.EmployeeCreationListener;
import it.redhat.demo.cache.specific.UseSpecificEntityMarshaller;
import it.redhat.demo.model.Company;
import it.redhat.demo.model.Employee;
import it.redhat.demo.model.GenericEntity;

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

	@Inject
	@UseSpecificEntityMarshaller
	private RemoteCacheManager cacheManager;

	@Inject
	@UseGenericEntityMarshaller
	private RemoteCacheManager oneToManyCacheManager;

	@Inject
	private EmployeeCreationListener listener;

	@PostConstruct
	private void onStartup() {
		cacheManager.start();

		RemoteCache<Object, Object> employeeCache = cacheManager.administration().getOrCreateCache( EMPLOYEE_CACHE, (String) null );
		employeeCache.addClientListener( listener );

		RemoteCache<Object, Object> companyCache = cacheManager.administration().getOrCreateCache( COMPANY_CACHE, (String) null );
		companyCache.addClientListener( listener );

		oneToManyCacheManager.start();

		oneToManyCacheManager.administration().getOrCreateCache( EMPLOYEE_CACHE, (String) null );
		oneToManyCacheManager.administration().getOrCreateCache( COMPANY_CACHE, (String) null );
	}

	@PreDestroy
	private void onShutdown() {
		cacheManager.stop();
		oneToManyCacheManager.stop();
	}

	@Produces
	public RemoteCache<Integer, Employee> getEmployeeCache() {
		return cacheManager.<Integer, Employee>getCache( EMPLOYEE_CACHE);
	}

	@Produces
	public RemoteCache<String, Company> getCompanyCache() {
		return cacheManager.<String, Company>getCache( COMPANY_CACHE );
	}

	@Produces
	public RemoteCache<Integer, GenericEntity> getEmployeeGenericCache() {
		return oneToManyCacheManager.<Integer, GenericEntity>getCache( EMPLOYEE_CACHE);
	}

	@Produces
	public RemoteCache<String, GenericEntity> getCompanyGenericCache() {
		return oneToManyCacheManager.<String, GenericEntity>getCache( COMPANY_CACHE );
	}

}
