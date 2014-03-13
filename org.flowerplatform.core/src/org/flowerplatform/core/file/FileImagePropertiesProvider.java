package org.flowerplatform.core.file;

import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FileImagePropertiesProvider extends PropertiesProvider {
	
	public FileImagePropertiesProvider() {
		super();
		this.setOrderIndex(100);
	}
	
	@Override
	public void populateWithProperties(Node node, Map<String, Object> options) {
		if (node.getProperties().get(NodePropertiesConstants.IS_DIRECTORY).equals("true")) {
			node.getProperties().put("icons", CorePlugin.getInstance().getResourceUrl("images/folder.gif"));
		} else {
			node.getProperties().put("icons", CorePlugin.getInstance().getResourceUrl("images/file.gif"));
		}
	}
	

}
