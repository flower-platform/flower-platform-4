package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.IS_DIRECTORY;
import static org.flowerplatform.core.NodePropertiesConstants.IS_SUBSCRIBABLE;
import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

import java.util.Map;

import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.freeplane.features.url.UrlManager;

/**
 * @author Mariana Gheorghe
 */
public class FreeplaneIsSubscribablePropertyProvider extends PropertiesProvider {

	public FreeplaneIsSubscribablePropertyProvider() {
		super();
		
		// must be invoked after the file properties providers
		// so the other properties are already set
		setOrderIndex(10000);
	}
	
	@Override
	public void populateWithProperties(Node node, Map<String, Object> options) {
		boolean isDirectory = Boolean.parseBoolean((String) node.getProperties().get(IS_DIRECTORY));
		if (!isDirectory) {
			String path = (String) node.getProperties().get(TEXT);
			if (path.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
				node.getProperties().put(IS_SUBSCRIBABLE, true);
			}
		}
	}
	
}
