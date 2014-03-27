package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_CREATION_TIME;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_LAST_ACCESS_TIME;
import static org.flowerplatform.core.CoreConstants.FILE_LAST_MODIFIED_TIME;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SIZE;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.IS_SUBSCRIBABLE;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;

import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.ServiceContext;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.controller.PropertiesProvider;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;

/**
 * @author Sebastian Solomon
 */
public class FileSystemControllers {
	
	public void registerControllers() {
		Map<String, Object> filePropertyMap = new HashMap<String, Object>();
		Map<String, Object> folderPropertyMap = new HashMap<String, Object>();
		folderPropertyMap.put(FILE_IS_DIRECTORY, true);
		filePropertyMap.put(FILE_IS_DIRECTORY, false);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.fileContainer")
		.addAdditiveController(CHILDREN_PROVIDER, new FileChildrenProvider())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController())
		.addAdditiveController(ADD_NODE_CONTROLLER, new FileAddNodeController())
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE_NODE_TYPE).setLabelAs("File")
			.setPropertiesAs(filePropertyMap)
			.setIconAs(CorePlugin.getInstance().getResourceUrl("images/file.gif"))
			.setOrderIndexAs(10))
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE_NODE_TYPE).setLabelAs("Folder")
			.setPropertiesAs(folderPropertyMap)
			.setIconAs(CorePlugin.getInstance().getResourceUrl("images/folder.gif"))
			.setOrderIndexAs(20));
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_SYSTEM_NODE_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new PropertiesProvider() {
			public void populateWithProperties(Node node, ServiceContext context) {
				node.getProperties().put(NAME, FILE_SYSTEM_NODE_TYPE);
			}
		})
		.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(IS_SUBSCRIBABLE, true))
		.addCategory("category.fileContainer");
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new FilePropertiesProvider())
		.addAdditiveController(PROPERTY_SETTER, new FilePropertySetter())
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_SIZE).setTypeAs("FileSize"))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_IS_DIRECTORY).setReadOnlyAs(true))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_CREATION_TIME).setReadOnlyAs(true).setTypeAs("Date"))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_LAST_MODIFIED_TIME).setReadOnlyAs(true).setTypeAs("Date"))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_LAST_ACCESS_TIME).setReadOnlyAs(true).setTypeAs("Date"))
		.addCategory("category.fileContainer");
	}

}
