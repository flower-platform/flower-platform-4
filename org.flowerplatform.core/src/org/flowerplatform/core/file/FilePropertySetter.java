package org.flowerplatform.core.file;

import static org.flowerplatform.core.NodePropertiesConstants.TEXT;

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
			case TEXT: 
				Object file;
				if (!node.getOrPopulateProperties().get(TEXT).equals(value.getPropertyValue())) {
					try {
						file = fileAccessController.getFile(node.getIdWithinResource());
						String parentPath = fileAccessController.getParent(file);
						Object parent = fileAccessController.getFile(parentPath);
						Object dest = fileAccessController.getFile(parent, value.getPropertyValue().toString());
						fileAccessController.rename(file, dest);
						node.getProperties().put(TEXT, value.getPropertyValue());
						//TODO
						//node.setIdWithinResource(fileAccessController.getAbsolutePath(dest));
						break;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
		}
	}

	@Override
	public void unsetProperty(Node node, String property) {
		node.getOrPopulateProperties().remove(property);
	}
	
}
