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
package org.flowerplatform.tests.core;

import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sebastian Solomon
 */
public class NodeServiceTest {
	private static NodeService nodeService;

	/**
	 * @author Mariana
	 */
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		/**
		 * @author Mariana
		 */
		class MockChildProvider extends AbstractController implements IChildrenProvider {
			@SuppressWarnings("rawtypes")
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				return null;
			}

			@Override
			public boolean hasChildren(Node node, ServiceContext context) {
				return false;
			}
		}
		
		IChildrenProvider spyProviderForTypeA = spy(new MockChildProvider());
		IChildrenProvider spyProviderForTypeB = spy(new MockChildProvider());
		IChildrenProvider spyProviderForTypeC = spy(new MockChildProvider());
		IChildrenProvider spyProviderForTypeFileSystem = spy(new MockChildProvider());
		IChildrenProvider spyProviderAll = spy(new MockChildProvider());
		
		// setPrioritys
		spyProviderForTypeA.setOrderIndex(-100);
		spyProviderForTypeB.setOrderIndex(0);
		spyProviderForTypeC.setOrderIndex(100);
		spyProviderForTypeFileSystem.setOrderIndex(200);
		spyProviderAll.setOrderIndex(Integer.MAX_VALUE);
		
		TypeDescriptorRegistry descriptorRegistry = new TypeDescriptorRegistry();
		nodeService = new NodeService(descriptorRegistry);
		
		// A
		TypeDescriptor nodeTypeDescriptorA = descriptorRegistry
				.getOrCreateTypeDescriptor("a");
		nodeTypeDescriptorA.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB);
						   
		
		when(spyProviderForTypeA.getChildren(eq(new Node(Utils.getUri("root", null, "0"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("a", null, "1"), FILE_NODE_TYPE),
						new Node(Utils.getUri("a", null, "2"), FILE_NODE_TYPE)));
		when(spyProviderForTypeA.getChildren(eq(new Node(Utils.getUri("a", null, "1"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("a", null, "12"), FILE_NODE_TYPE),
						new Node(Utils.getUri("a", null, "3"), FILE_NODE_TYPE)));

		when(spyProviderForTypeA.getChildren(eq(new Node(Utils.getUri("a", null, "2"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("a", null, "4"), FILE_NODE_TYPE),
						new Node(Utils.getUri("a", null, "5"), FILE_NODE_TYPE)));

		// B
		TypeDescriptor nodeTypeDescriptorB = descriptorRegistry
				.getOrCreateTypeDescriptor("b");
		
		nodeTypeDescriptorB.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll);
		nodeTypeDescriptorB.addCategory("category.fileSystem");
		
		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri("a", null, "2"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("b", null, "6"), FILE_NODE_TYPE),
						new Node(Utils.getUri("b", null, "7"), FILE_NODE_TYPE)));

		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri("b", null, "10"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("b", null, "11"), FILE_NODE_TYPE),
						new Node(Utils.getUri("b", null, "13"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri("b", null, "14"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("b", null, "15"), FILE_NODE_TYPE),
						new Node(Utils.getUri("b", null, "16"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri("root", null, "0"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("b", null, "10"), FILE_NODE_TYPE),
						new Node(Utils.getUri("b", null, "14"), FILE_NODE_TYPE)));
		
		// C
		TypeDescriptor nodeTypeDescriptorC = descriptorRegistry
				.getOrCreateTypeDescriptor("c");
		nodeTypeDescriptorC.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
		   				   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
		   				   .addCategory("category.fileSystem");
		
		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri("a", null, "2"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("c", null, "8"), FILE_NODE_TYPE),
						new Node(Utils.getUri("c", null, "9"), FILE_NODE_TYPE)));

		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri("c", null, "17"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("c", null, "19"), FILE_NODE_TYPE),
						new Node(Utils.getUri("c", null, "20"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri("c", null, "18"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("c", null, "21"), FILE_NODE_TYPE),
						new Node(Utils.getUri("c", null, "22"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri("root", null, "0"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("c", null, "17"), FILE_NODE_TYPE),
						new Node(Utils.getUri("c", null, "18"), FILE_NODE_TYPE)));
		
		// root
		TypeDescriptor nodeTypeDescriptorRoot = descriptorRegistry
				.getOrCreateTypeDescriptor("root");
		
		nodeTypeDescriptorRoot.addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
							  .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
							  .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
							  .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC);
		
		// fileSystem
		TypeDescriptor fileSystemTypeDescriptor = descriptorRegistry
				.getOrCreateCategoryTypeDescriptor("category.fileSystem");
		fileSystemTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeFileSystem);
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri("b", null, "10"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("fileSystem", null, "23"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri("b", null, "14"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("fileSystem", null, "24"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri("c", null, "17"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("fileSystem", null, "25"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri("c", null, "18"), FILE_NODE_TYPE)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("fileSystem", null, "26"), FILE_NODE_TYPE)));
		// all
		when(spyProviderAll.getChildren(any(Node.class), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
	}
	
	/**
	 * @see <a href=
	 *      "D:\data\java_work\git_repo\flower-platform-4\org.flowerplatform.tests\etc\testTreeStructure.txt"
	 *      >etc\testTreeStructure.txt file</a>
	 */
	@Test
	public void testPriority() {
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("root", null, "0"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("a", null, "1"), FILE_NODE_TYPE),
				new Node(Utils.getUri("a", null, "2"), FILE_NODE_TYPE),
				new Node(Utils.getUri("b", null, "10"), FILE_NODE_TYPE),
				new Node(Utils.getUri("b", null, "14"), FILE_NODE_TYPE),
				new Node(Utils.getUri("c", null, "17"), FILE_NODE_TYPE),
				new Node(Utils.getUri("c", null, "18"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
		
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("a", null, "1"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("a", null, "12"), FILE_NODE_TYPE),
				new Node(Utils.getUri("a", null, "3"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
		
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("a", null, "2"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("a", null, "4"), FILE_NODE_TYPE),
				new Node(Utils.getUri("a", null, "5"), FILE_NODE_TYPE),
				new Node(Utils.getUri("b", null, "6"), FILE_NODE_TYPE),
				new Node(Utils.getUri("b", null, "7"), FILE_NODE_TYPE),
				new Node(Utils.getUri("c", null, "8"), FILE_NODE_TYPE),
				new Node(Utils.getUri("c", null, "9"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
		
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("b", null, "10"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("b", null, "11"), FILE_NODE_TYPE),
				new Node(Utils.getUri("b", null, "13"), FILE_NODE_TYPE),
				new Node(Utils.getUri("fileSystem", null, "23"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
		
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("b", null, "14"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("b", null, "15"), FILE_NODE_TYPE),
				new Node(Utils.getUri("b", null, "16"), FILE_NODE_TYPE),
				new Node(Utils.getUri("fileSystem", null, "24"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
		
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("c", null, "17"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("c", null, "19"), FILE_NODE_TYPE),
				new Node(Utils.getUri("c", null, "20"), FILE_NODE_TYPE),
				new Node(Utils.getUri("fileSystem", null, "25"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
		
		assertEquals(nodeService.getChildren(new Node(Utils.getUri("c", null, "18"), FILE_NODE_TYPE), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node(Utils.getUri("c", null, "21"), FILE_NODE_TYPE),
				new Node(Utils.getUri("c", null, "22"), FILE_NODE_TYPE),
				new Node(Utils.getUri("fileSystem", null, "26"), FILE_NODE_TYPE),
				new Node(Utils.getUri("all", null, "0"), FILE_NODE_TYPE)));
	}
	
}
