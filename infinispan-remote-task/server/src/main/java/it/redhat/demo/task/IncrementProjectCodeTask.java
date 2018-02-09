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
public class IncrementProjectCodeTask implements ServerTask<Project> {

	private static final Logger log = LoggerFactory.getLogger( CiaoRemoteTask.class );

	private static final String CACHE_NAME = "projects";
	private static final String CACHE_PARAM_KEY = "name";

	private TaskContext taskContext;

	@Override
	public String getName() {
		return IncrementProjectCodeTask.class.getSimpleName();
	}

	@Override
	public void setTaskContext(TaskContext taskContext) {
		this.taskContext = taskContext;
	}

	@Override
	public Project call() throws Exception {

		Cache<String, Project> cache = taskContext.getCacheManager().getCache( CACHE_NAME );
		String projectName = (String) taskContext.getParameters().get().get( CACHE_PARAM_KEY );

		log.info( "Executing task. Updating project: {}", projectName );

		Project project = cache.get( projectName );

		if (project == null) {
			log.warn( "Cache Entry Project not found!" );
			return null;
		}

		project.setCode( project.getCode() + 1 );
		cache.put( projectName, project );

		log.info( "Executed task. Update project: {}", projectName );

		return project;
	}

}
