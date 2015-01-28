package org.flowerplatform.tests.diff_update.entity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class ObjectAction extends AbstractEntity {

	private ObjectActionGroup objectActionGroup;
	
	public ObjectAction(int id) {
		super(id);
	}

	public ObjectActionGroup getObjectActionGroup() {
		return objectActionGroup;
	}

	public void setObjectActionGroup(ObjectActionGroup objectActionGroup) {
		this.objectActionGroup = objectActionGroup;
	}
	
}
