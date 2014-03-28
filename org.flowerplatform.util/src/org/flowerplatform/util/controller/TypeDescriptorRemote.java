package org.flowerplatform.util.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Mariana Gheorghe
 */
public class TypeDescriptorRemote {

	private String type;
	
	private List<String> categories;
	
	private Map<String, IDescriptor> singleControllers = new HashMap<String, IDescriptor>();
	
	private Map<String, List<IDescriptor>> additiveControllers = new HashMap<String, List<IDescriptor>>();

	public TypeDescriptorRemote(String type, List<String> categories) {
		this.type = type;
		this.categories = categories;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Map<String, IDescriptor> getSingleControllers() {
		return singleControllers;
	}

	public void setSingleControllers(Map<String, IDescriptor> singleControllers) {
		this.singleControllers = singleControllers;
	}

	public Map<String, List<IDescriptor>> getAdditiveControllers() {
		return additiveControllers;
	}

	public void setAdditiveControllers(
			Map<String, List<IDescriptor>> additiveControllers) {
		this.additiveControllers = additiveControllers;
	}
	
	
}
