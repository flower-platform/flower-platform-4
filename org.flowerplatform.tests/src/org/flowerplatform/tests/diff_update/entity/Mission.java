package org.flowerplatform.tests.diff_update.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class Mission extends AbstractEntity {

	private HumanResourceSchedule humanResourceSchedule; 

	public Mission(int id) {
		super(id);
	}
	
	private List<ObjectActionGroup> objectActionGroups = new ArrayList<>();

	public List<ObjectActionGroup> getObjectActionGroups() {
		return objectActionGroups;
	}

	public void setObjectActionGroups(List<ObjectActionGroup> objectActionGroups) {
		this.objectActionGroups = objectActionGroups;
	}


	public HumanResourceSchedule getHumanResourceSchedule() {
		return humanResourceSchedule;
	}

	
	public void setHumanResourceSchedule(HumanResourceSchedule humanResourceSchedule) {
		this.humanResourceSchedule = humanResourceSchedule;
	} 
	
}
