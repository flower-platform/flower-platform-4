package org.flowerplatform.tests.diff_update.entity;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class HumanResource extends AbstractEntity {
	
	private Mission mission; 
	
	private List<AbstractEntity> humanResourceSchedules;
	
	public HumanResource(int id) {
		super(id);
	}

	public boolean isRoot() {
		return true;
	}
	
	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public List<AbstractEntity> getHumanResourceSchedules() {
		return humanResourceSchedules;
	}

	public void setHumanResourceSchedules(List<AbstractEntity> humanResourceSchedules) {
		this.humanResourceSchedules = humanResourceSchedules;
	}

}
