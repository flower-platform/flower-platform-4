package org.flowerplatform.core.file;

import static org.flowerplatform.core.NodePropertiesConstants.NAME;

import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.controller.PropertySetter;
import org.flowerplatform.core.node.controller.PropertyValueWrapper;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Sebastian Solomon
 */
public class FilePropertySetter extends PropertySetter {
	
	@Override
	public void setProperty(Node node, String property, PropertyValueWrapper value, Map<String, Object> options) {
		IFileAccessController fileAccessController = CorePlugin.getInstance().getFileAccessController();
		if (NAME.equals(property)) {
			Object file;
			if (!node.getOrPopulateProperties().get(NAME).equals(value.getPropertyValue())) {
				try {
					throw new UnsupportedOperationException();
//					file = fileAccessController.getFile(node.getIdWithinResource());
//					String parentPath = fileAccessController.getParent(file);
//					Object parent = fileAccessController.getFile(parentPath);
//					Object dest = fileAccessController.getFile(parent, value.getPropertyValue().toString());
//					if (fileAccessController.exists(dest)) {
//						throw new RuntimeException("There is already a file with the same name in this location.");
//					}
//					if (!fileAccessController.rename(file, dest)) {
//						throw new RuntimeException("The filename, directory name, or volume label syntax is incorrect");
//					}
//					node.getProperties().put(NAME, value.getPropertyValue());
//					node.setIdWithinResource(fileAccessController.getAbsolutePath(dest));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	@Override
	public void unsetProperty(Node node, String property, Map<String, Object> options) {
		node.getOrPopulateProperties().remove(property);
	}
	
}
