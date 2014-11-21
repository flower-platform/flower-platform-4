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
import static org.flowerplatform.core.CoreConstants.FILE_CONTAINER_CATEGORY;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
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
import org.flowerplatform.tests.EclipseIndependentTestBase;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.ITypeDescriptorRegistryProvider;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sebastian Solomon
 * @author Mariana Gheorghe
 */
public class NodeServiceTest extends EclipseIndependentTestBase {
	
	private static NodeService nodeService;
	
	private static final String TYPE_A = "a";
	private static final String TYPE_B = "b";
	private static final String TYPE_C = "c";
	private static final String TYPE_ROOT = "r";
	
	private static TypeDescriptorRegistry descriptorRegistry = new TypeDescriptorRegistry();

	/**
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		/**
		 */
		class MockChildProvider extends AbstractController implements IChildrenProvider {
			
			public MockChildProvider() {
				super();
				setSharedControllerAllowed(true);
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			public List<Node> getChildren(Node node, ServiceContext context) {
				return null;
			}

			@Override
			public boolean hasChildren(Node node, @SuppressWarnings("rawtypes") ServiceContext context) {
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
		
		nodeService = new NodeService(new ITypeDescriptorRegistryProvider() {
			
			@Override
			public TypeDescriptorRegistry getTypeDescriptorRegistry(Object model) {
				return NodeServiceTest.descriptorRegistry;
			}
		});
		
		// A
		TypeDescriptor nodeTypeDescriptorA = descriptorRegistry.getOrCreateTypeDescriptor(TYPE_A);
		nodeTypeDescriptorA.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB);
						   
		
		when(spyProviderForTypeA.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "0"), TYPE_ROOT)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "1"), TYPE_A),
						new Node(Utils.getUri(FILE_SCHEME, null, "2"), TYPE_A)));
		when(spyProviderForTypeA.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "1"), TYPE_A)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "12"), TYPE_A),
						new Node(Utils.getUri(FILE_SCHEME, null, "3"), TYPE_A)));

		when(spyProviderForTypeA.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "2"), TYPE_A)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "4"), TYPE_A),
						new Node(Utils.getUri(FILE_SCHEME, null, "5"), TYPE_A)));

		// B
		TypeDescriptor nodeTypeDescriptorB = descriptorRegistry.getOrCreateTypeDescriptor(TYPE_B);
		nodeTypeDescriptorB.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
						   .addCategory(FILE_CONTAINER_CATEGORY);
		
		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "2"), TYPE_A)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "6"), TYPE_B),
						new Node(Utils.getUri(FILE_SCHEME, null, "7"), TYPE_B)));

		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "10"), TYPE_B)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "11"), TYPE_B),
						new Node(Utils.getUri(FILE_SCHEME, null, "13"), TYPE_B)));
		
		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "14"), TYPE_B)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "15"), TYPE_B),
						new Node(Utils.getUri(FILE_SCHEME, null, "16"), TYPE_B)));
		
		when(spyProviderForTypeB.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "0"), TYPE_ROOT)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "10"), TYPE_B),
						new Node(Utils.getUri(FILE_SCHEME, null, "14"), TYPE_B)));
		
		// C
		TypeDescriptor nodeTypeDescriptorC = descriptorRegistry.getOrCreateTypeDescriptor(TYPE_C);
		nodeTypeDescriptorC.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
		   				   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
		   				   .addCategory(FILE_CONTAINER_CATEGORY);
		
		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "2"), TYPE_A)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "8"), TYPE_C),
						new Node(Utils.getUri(FILE_SCHEME, null, "9"), TYPE_C)));

		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "17"), TYPE_C)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "19"), TYPE_C),
						new Node(Utils.getUri(FILE_SCHEME, null, "20"), TYPE_C)));
		
		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "18"), TYPE_C)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "21"), TYPE_C),
						new Node(Utils.getUri(FILE_SCHEME, null, "22"), TYPE_C)));
		
		when(spyProviderForTypeC.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "0"), TYPE_ROOT)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "17"), TYPE_C),
						new Node(Utils.getUri(FILE_SCHEME, null, "18"), TYPE_C)));
		
		// root
		TypeDescriptor nodeTypeDescriptorRoot = descriptorRegistry.getOrCreateTypeDescriptor(TYPE_ROOT);
		nodeTypeDescriptorRoot.addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
							  .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
							  .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
							  .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC);
		
		// fileSystem
		TypeDescriptor fileSystemTypeDescriptor = descriptorRegistry.getOrCreateCategoryTypeDescriptor(FILE_CONTAINER_CATEGORY);
		fileSystemTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeFileSystem);
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "10"), TYPE_B)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "23"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "14"), TYPE_B)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "24"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "17"), TYPE_C)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "25"), FILE_NODE_TYPE)));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node(Utils.getUri(FILE_SCHEME, null, "18"), TYPE_C)), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "26"), FILE_NODE_TYPE)));
		// all
		when(spyProviderAll.getChildren(any(Node.class), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)));
	}
	
	/**
	 * @see <a href=
	 *      "D:\data\java_work\git_repo\flower-platform-4\org.flowerplatform.tests\etc\testTreeStructure.txt"
	 *      >etc\testTreeStructure.txt file</a>
	 */
	@Test
	public void testPriority() {
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "1"), TYPE_A),
				new Node(Utils.getUri(FILE_SCHEME, null, "2"), TYPE_A),
				new Node(Utils.getUri(FILE_SCHEME, null, "10"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "14"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "17"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "18"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "0"), TYPE_ROOT), new ServiceContext<NodeService>(nodeService)));
		
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "12"), TYPE_A),
				new Node(Utils.getUri(FILE_SCHEME, null, "3"), TYPE_A),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "1"), TYPE_A), new ServiceContext<NodeService>(nodeService)));
		
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "4"), TYPE_A),
				new Node(Utils.getUri(FILE_SCHEME, null, "5"), TYPE_A),
				new Node(Utils.getUri(FILE_SCHEME, null, "6"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "7"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "8"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "9"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "2"), TYPE_A), new ServiceContext<NodeService>(nodeService)));
		
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "11"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "13"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "23"), FILE_NODE_TYPE),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "10"), TYPE_B), new ServiceContext<NodeService>(nodeService)));
		
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "15"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "16"), TYPE_B),
				new Node(Utils.getUri(FILE_SCHEME, null, "24"), FILE_NODE_TYPE),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "14"), TYPE_B), new ServiceContext<NodeService>(nodeService)));
		
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "19"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "20"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "25"), FILE_NODE_TYPE),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "17"), TYPE_C), new ServiceContext<NodeService>(nodeService)));
		
		assertEquals(Arrays.asList(
				new Node(Utils.getUri(FILE_SCHEME, null, "21"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "22"), TYPE_C),
				new Node(Utils.getUri(FILE_SCHEME, null, "26"), FILE_NODE_TYPE),
				new Node(Utils.getUri(FILE_SCHEME, null, "0"), FILE_NODE_TYPE)),
				nodeService.getChildren(new Node(Utils.getUri(FILE_SCHEME, null, "18"), TYPE_C), new ServiceContext<NodeService>(nodeService)));
	}
	
}
