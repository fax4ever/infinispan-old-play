/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package it.redhat.demo.task;

import org.infinispan.Cache;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
public class CreateProjectByNameTask implements ServerTask<Project> {

	private static final Logger LOG = LoggerFactory.getLogger( CreateProjectByNameTask.class );

	private static final String CACHE_NAME = "projects";
	private static final String CACHE_PARAM_KEY = "name";

	private TaskContext taskContext;

	@Override
	public String getName() {
		return CreateProjectByNameTask.class.getSimpleName();
	}

	@Override
	public void setTaskContext(TaskContext taskContext) {
		this.taskContext = taskContext;
	}

	@Override
	public Project call() throws Exception {

		Cache<String, Project> cache = taskContext.getCacheManager().getCache( CACHE_NAME );
		String projectName = (String) taskContext.getParameters().get().get( CACHE_PARAM_KEY );

		LOG.info( "Executing task {}. Creating project: {}", getName(), projectName );

		Project project = new Project();
		project.setCode( 1 );
		project.setDescription( projectName );
		project.setName( projectName );

		project = cache.put( projectName, project );
		LOG.info( "Executed task {}. Project {} created", getName(), projectName );

		return cache.get( projectName );
	}

}
