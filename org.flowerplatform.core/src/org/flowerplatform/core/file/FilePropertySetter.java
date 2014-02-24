package org.flowerplatform.core.file;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FilePropertySetter extends PropertySetter {
	
	private static IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper value) {
		
		switch (property) {
			case "body": 
//			case "name":
				Object file;
				if (!node.getProperties().get("body").equals(value)) {
					try {
						file = fileAccessController.getFile(node.getIdWithinResource());
						String parentPath = fileAccessController.getParent(file);
						Object parent = fileAccessController.getFile(parentPath);
						Object dest = fileAccessController.getFile(parent, value.toString());
						fileAccessController.rename(file, dest);
						node.getProperties().put("body", value);
						break;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			case "isDirectory":
				node.getProperties().put("isDirectory", value);
				break;
		}
	}

	@Override
	public void unsetProperty(Node node, String property) {
		node.getOrPopulateProperties().remove(property);
	}
	
}
