/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.ADD_CHILD_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_CONTAINER_CATEGORY;
import static org.flowerplatform.core.CoreConstants.FILE_CREATION_TIME;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_LAST_ACCESS_TIME;
import static org.flowerplatform.core.CoreConstants.FILE_LAST_MODIFIED_TIME;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.FILE_SIZE;
import static org.flowerplatform.core.CoreConstants.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.util.UtilConstants.FEATURE_PROPERTY_DESCRIPTORS;
import static org.flowerplatform.util.UtilConstants.PROPERTY_EDITOR_TYPE_BOOLEAN;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_DATE;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.AddChildDescriptor;
import org.flowerplatform.core.node.remote.PropertyDescriptor;

/**
 * @author Sebastian Solomon
 */
public class FileSystemControllers {
	
	/**
	 *@author see class
	 **/
	public void registerControllers() {
		FileChildrenController fileChildrenController = new FileChildrenController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(FILE_CONTAINER_CATEGORY)
		.addAdditiveController(CHILDREN_PROVIDER, fileChildrenController)
		.addAdditiveController(REMOVE_NODE_CONTROLLER, fileChildrenController)
		.addAdditiveController(ADD_NODE_CONTROLLER, fileChildrenController)
		.addAdditiveController(ADD_CHILD_DESCRIPTOR, new AddChildDescriptor().setChildTypeAs(FILE_NODE_TYPE).setOrderIndexAs(10));
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_SYSTEM_NODE_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, new FileSystemNodeController())
		.addCategory(FILE_CONTAINER_CATEGORY);
		
		FilePropertiesController filePropertiesController = new FilePropertiesController();
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(FILE_NODE_TYPE)
		.addAdditiveController(PROPERTIES_PROVIDER, filePropertiesController)
		.addAdditiveController(PROPERTY_SETTER, filePropertiesController)
		.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(NAME).setReadOnlyAs(true).setContributesToCreationAs(true).setOrderIndexAs(-10))
		.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FILE_IS_DIRECTORY).setReadOnlyAs(true).setContributesToCreationAs(true)
				.setTypeAs(PROPERTY_EDITOR_TYPE_BOOLEAN).setOrderIndexAs(-5))
		.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FILE_SIZE).setReadOnlyAs(true))
		.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FILE_CREATION_TIME).setReadOnlyAs(true)
				.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DATE).setOrderIndexAs(10))
		.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FILE_LAST_MODIFIED_TIME).setReadOnlyAs(true)
				.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DATE).setOrderIndexAs(11))
		.addAdditiveController(FEATURE_PROPERTY_DESCRIPTORS, new PropertyDescriptor().setNameAs(FILE_LAST_ACCESS_TIME).setReadOnlyAs(true)
				.setTypeAs(PROPERTY_DESCRIPTOR_TYPE_DATE).setOrderIndexAs(12))
		.addCategory(FILE_CONTAINER_CATEGORY);
		
		CorePlugin.getInstance().getResourceService().addResourceHandler(FILE_SCHEME, new FileSystemResourceHandler());
	}

}
