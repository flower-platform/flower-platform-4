package org.flowerplatform.core.file;

import static org.flowerplatform.core.CorePlugin.FILE_NODE_TYPE;
import static org.flowerplatform.core.CorePlugin.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.NodePropertiesConstants.FILE_CREATION_TIME;
import static org.flowerplatform.core.NodePropertiesConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.NodePropertiesConstants.FILE_LAST_ACCESS_TIME;
import static org.flowerplatform.core.NodePropertiesConstants.FILE_LAST_MODIFIED_TIME;
import static org.flowerplatform.core.NodePropertiesConstants.FILE_SIZE;
import static org.flowerplatform.core.NodePropertiesConstants.IS_SUBSCRIBABLE;
import static org.flowerplatform.core.NodePropertiesConstants.NAME;
import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.node.remote.AddChildDescriptor.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.BOOLEAN;
import static org.flowerplatform.core.node.remote.PropertyDescriptor.PROPERTY_DESCRIPTOR;

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
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor("category.fileContainer")
		.addAdditiveController(CHILDREN_PROVIDER, new FileChildrenProvider())
		.addAdditiveController(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController())
		.addAdditiveController(ADD_NODE_CONTROLLER, new FileAddNodeController())
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE_NODE_TYPE).setLabelAs("File/Folder")
			.setIconAs(CorePlugin.getInstance().getResourceUrl("images/file.gif")));
	
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
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(NAME).setTitleAs(CorePlugin.getInstance().getMessage("file.name")).setContributeToCreationAs(true).setIsMandatoryAs(true).setOrderIndexAs(-10))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_SIZE).setTitleAs(CorePlugin.getInstance().getMessage("file.size")).setTypeAs("FileSize"))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_IS_DIRECTORY).setTitleAs(CorePlugin.getInstance().getMessage("file.is.directory")).setReadOnlyAs(true).setContributeToCreationAs(true).setIsMandatoryAs(true).setTypeAs(BOOLEAN).setOrderIndexAs(-5))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_CREATION_TIME).setTitleAs(CorePlugin.getInstance().getMessage("file.creation.time")).setReadOnlyAs(true).setTypeAs("Date").setOrderIndexAs(10))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_LAST_MODIFIED_TIME).setTitleAs(CorePlugin.getInstance().getMessage("file.modified.time")).setReadOnlyAs(true).setTypeAs("Date").setOrderIndexAs(11))
		.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(FILE_LAST_ACCESS_TIME).setTitleAs(CorePlugin.getInstance().getMessage("file.accessed.time")).setReadOnlyAs(true).setTypeAs("Date").setOrderIndexAs(12))
		.addCategory("category.fileContainer");
	}

}
