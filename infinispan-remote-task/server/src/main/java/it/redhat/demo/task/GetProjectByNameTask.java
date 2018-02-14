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
public class GetProjectByNameTask extends CacheTask implements ServerTask<Project> {

	private static final Logger LOG = LoggerFactory.getLogger( IncrementProjectCodeTask.class );

	private static final String CACHE_PARAM_KEY = "name";

	private TaskContext taskContext;

	@Override
	public String getName() {
		return GetProjectByNameTask.class.getSimpleName();
	}

	@Override
	public void setTaskContext(TaskContext taskContext) {
		this.taskContext = taskContext;
	}

	@Override
	public Project call() throws Exception {

		Cache<String, Project> cache = getCache( taskContext );
		String projectName = (String) taskContext.getParameters().get().get( CACHE_PARAM_KEY );

		LOG.info( "Executing task. Get project: {}", projectName );

		Project project = cache.get( projectName );

		if (project == null) {
			LOG.warn( "Cache Entry Project with name {} not found!", projectName );
			return null;
		}

		LOG.info( "Executed task. Project {} retrieved", project );

		return project;
	}
}
