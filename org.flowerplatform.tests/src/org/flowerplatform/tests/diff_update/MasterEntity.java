package org.flowerplatform.tests.diff_update;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class MasterEntity extends AbstractEntity {
	
	private long id;
	
	private String name;

	private List<Object> details = new ArrayList<>();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getDetails() {
		return details;
	}

	public void setDetails(List<Object> children) {
		this.details = children;
	}
	
}
