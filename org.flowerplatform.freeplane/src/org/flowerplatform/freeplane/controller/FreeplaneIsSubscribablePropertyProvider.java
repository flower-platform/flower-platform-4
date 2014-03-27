package org.flowerplatform.freeplane.controller;

import static org.flowerplatform.core.NodePropertiesConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.NodePropertiesConstants.IS_SUBSCRIBABLE;
import static org.flowerplatform.core.NodePropertiesConstants.NAME;

import java.util.Map;

import org.flowerplatform.core.ServiceContext;
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
	public void populateWithProperties(Node node, ServiceContext context) {
		boolean isDirectory = (boolean)node.getProperties().get(FILE_IS_DIRECTORY);
		if (!isDirectory) {
			String path = (String) node.getProperties().get(NAME);
			if (path.endsWith(UrlManager.FREEPLANE_FILE_EXTENSION)) {
				node.getProperties().put(IS_SUBSCRIBABLE, true);
			}
		}
	}
	
}
