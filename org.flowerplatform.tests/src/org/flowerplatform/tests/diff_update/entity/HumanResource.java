package org.flowerplatform.tests.diff_update.entity;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class HumanResource extends AbstractEntity {
	
	private Mission mission; 
	
	private List<HumanResourceSchedule> humanResourceSchedules;
	
	public HumanResource(int id) {
		super(id);
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public List<HumanResourceSchedule> getHumanResourceSchedules() {
		return humanResourceSchedules;
	}

	public void setHumanResourceSchedules(List<HumanResourceSchedule> humanResourceSchedules) {
		this.humanResourceSchedules = humanResourceSchedules;
	}
	
}
