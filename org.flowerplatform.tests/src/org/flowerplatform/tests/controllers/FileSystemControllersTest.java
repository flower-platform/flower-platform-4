package org.flowerplatform.tests.controllers;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.flowerplatform.core.file.FileAddNodeControlller;
import org.flowerplatform.core.file.FileChildrenProvider;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.file.FilePropertySetter;
import org.flowerplatform.core.file.FileRemoveNodeController;
import org.flowerplatform.core.fileSystem.FileSystemPropertiesProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.tests.TestUtil;
import org.flowerplatform.util.type_descriptor.TypeDescriptor;
import org.flowerplatform.util.type_descriptor.TypeDescriptorRegistry;
import org.junit.Test;

public class FileSystemControllersTest {
	
	
	@Test
	public void test() {
		String resoucesDir = TestUtil.getResourcesDir(this.getClass());
		TypeDescriptorRegistry descriptorRegistry = new TypeDescriptorRegistry();
		NodeService  nodeService = new NodeService(descriptorRegistry);
		
		TypeDescriptor fileNodeTypeDescriptor = descriptorRegistry
								.getOrCreateNodeTypeDescriptor("fileNode");
		fileNodeTypeDescriptor.addControllerToList(CHILDREN_PROVIDER, new FileChildrenProvider());
		fileNodeTypeDescriptor.addControllerToList(PROPERTIES_PROVIDER, new FilePropertiesProvider());
		fileNodeTypeDescriptor.addControllerToList(ADD_NODE_CONTROLLER, new FileAddNodeControlller());
		fileNodeTypeDescriptor.addControllerToList(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_SETTER, new FilePropertySetter());
		
		TypeDescriptor fileSystemTypeDescriptor = descriptorRegistry
				.getOrCreateNodeTypeDescriptor("fileSystem");
		fileSystemTypeDescriptor.addControllerToList(CHILDREN_PROVIDER,	new FileChildrenProvider());
		fileSystemTypeDescriptor.addControllerToList(PROPERTIES_PROVIDER, new FileSystemPropertiesProvider());
		fileSystemTypeDescriptor.addControllerToList(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileSystemTypeDescriptor.addControllerToList(ADD_NODE_CONTROLLER, new FileAddNodeControlller());

		assertEquals(nodeService.getChildren(new Node(resoucesDir, null, "fileNode"), false), Arrays.asList(
								new Node(),
								new Node()));

	}

}
