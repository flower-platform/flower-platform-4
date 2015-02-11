package org.flowerplatform.tests.diff_update.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class Task extends AbstractEntity {
	
	public boolean isRootEntity = true;

	public Task(int id) {
		super(id);
	}
	
	private List<ObjectActionGroup> objectActionGroups = new ArrayList<>();

	public List<ObjectActionGroup> getObjectActionGroups() {
		return objectActionGroups;
	}

	public void setObjectActionGroups(List<ObjectActionGroup> objectActionGroups) {
		this.objectActionGroups = objectActionGroups;
	}
	
}
