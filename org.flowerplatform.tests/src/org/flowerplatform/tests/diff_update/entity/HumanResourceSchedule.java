package org.flowerplatform.tests.diff_update.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class HumanResourceSchedule extends AbstractEntity {

	public HumanResourceSchedule(int id) {
		super(id);
	}
	
	private List<Mission> missions = new ArrayList<>();
	
	private HumanResource humanResource;

	public List<Mission> getMissions() {
		return missions;
	}

	public void setMissions(List<Mission> missions) {
		this.missions = missions;
	}

	public HumanResource getHumanResource() {
		return humanResource;
	}

	public void setHumanResource(HumanResource humanResource) {
		this.humanResource = humanResource;
	}
	
}
