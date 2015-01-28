package org.flowerplatform.tests.diff_update.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class ObjectActionGroup extends AbstractEntity {

	private Task task;
	
	public ObjectActionGroup(int id) {
		super(id);
	}
	
	private List<ObjectAction> objectActions = new ArrayList<>();

	public List<ObjectAction> getObjectActions() {
		return objectActions;
	}

	public void setObjectActions(List<ObjectAction> objectActions) {
		this.objectActions = objectActions;
	}

	
	public Task getTask() {
		return task;
	}

	
	public void setTask(Task task) {
		this.task = task;
	}

}
