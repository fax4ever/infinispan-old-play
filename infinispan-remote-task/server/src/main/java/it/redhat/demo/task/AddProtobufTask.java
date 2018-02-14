package it.redhat.demo.task;

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.remote.ProtobufMetadataManager;
import org.infinispan.tasks.ServerTask;
import org.infinispan.tasks.TaskContext;
import org.infinispan.tasks.TaskExecutionMode;

import it.redhat.demo.model.Project;

/**
 * @author Fabio Massimo Ercoli
 */
public class AddProtobufTask implements ServerTask {

	private TaskContext ctx;

	@Override
	public void setTaskContext(TaskContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public String getName() {
		return AddProtobufTask.class.getSimpleName();
	}

	@Override
	public Object call() throws Exception {
		EmbeddedCacheManager cm = ctx.getCache().get().getCacheManager();

		ProtobufMetadataManager protobufMetadataManager =
				cm.getGlobalComponentRegistry().getComponent(ProtobufMetadataManager.class);

		protobufMetadataManager.registerMarshaller(new Project.Marshaller());
		return null;
	}

	@Override
	public TaskExecutionMode getExecutionMode() {
		// Registering protofile should be done in all nodes
		return TaskExecutionMode.ALL_NODES;
	}

}
