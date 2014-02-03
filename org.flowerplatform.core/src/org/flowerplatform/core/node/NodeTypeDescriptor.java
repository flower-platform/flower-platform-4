package org.flowerplatform.core.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;

/**
 * @author Cristian Spiescu
 */
public class NodeTypeDescriptor {

	private List<String> categories;
	
	public List<String> getCategories() {
		if (categories == null) {
			categories = Collections.emptyList();
		}
		return categories;
	}
	
	public NodeTypeDescriptor addCategory(String category) {
		if (categories == null) {
			categories = new ArrayList<String>();
		}
		categories.add(category);
		return this;
	}
	
	private List<ChildrenProvider> childrenProviders;

	public List<ChildrenProvider> getChildrenProviders() {
		if (childrenProviders == null) {
			return Collections.emptyList();
		}
		return childrenProviders;
	}
	
	public NodeTypeDescriptor addChildrenProvider(ChildrenProvider provider) {
		if (childrenProviders == null) {
			childrenProviders = new ArrayList<ChildrenProvider>();
		}
		childrenProviders.add(provider);
		return this;
	}
	
	private List<PropertiesProvider<?>> propertiesProviders;

	public List<PropertiesProvider<?>> getPropertiesProviders() {
		if (propertiesProviders == null) {
			return Collections.emptyList();
		}
		return propertiesProviders;
	}

	public NodeTypeDescriptor addPropertiesProvider(PropertiesProvider<?> provider) {
		if (propertiesProviders == null) {
			propertiesProviders = new ArrayList<PropertiesProvider<?>>();
		}
		propertiesProviders.add(provider);
		return this;
	}

}
