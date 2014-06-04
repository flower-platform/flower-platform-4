package org.flowerplatform.tests.core;

import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
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

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
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
						   
		
		when(spyProviderForTypeA.getChildren(eq(new Node("root", null, "0")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("a", null, "1"),
						new Node("a", null, "2")));
		when(spyProviderForTypeA.getChildren(eq(new Node("a", null, "1")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("a", null, "12"),
						new Node("a", null, "3")));

		when(spyProviderForTypeA.getChildren(eq(new Node("a", null, "2")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("a", null, "4"),
						new Node("a", null, "5")));

		// B
		TypeDescriptor nodeTypeDescriptorB = descriptorRegistry
				.getOrCreateTypeDescriptor("b");
		
		nodeTypeDescriptorB.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll);
		nodeTypeDescriptorB.addCategory("category.fileSystem");
		
		when(spyProviderForTypeB.getChildren(eq(new Node("a", null, "2")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "6"),
						new Node("b", null, "7")));

		when(spyProviderForTypeB.getChildren(eq(new Node("b", null, "10")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "11"),
						new Node("b", null, "13")));
		
		when(spyProviderForTypeB.getChildren(eq(new Node("b", null, "14")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "15"),
						new Node("b", null, "16")));
		
		when(spyProviderForTypeB.getChildren(eq(new Node("root", null, "0")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "10"),
						new Node("b", null, "14")));
		
		// C
		TypeDescriptor nodeTypeDescriptorC = descriptorRegistry
				.getOrCreateTypeDescriptor("c");
		nodeTypeDescriptorC.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
		   				   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
		   				   .addCategory("category.fileSystem");
		
		when(spyProviderForTypeC.getChildren(eq(new Node("a", null, "2")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "8"),
						new Node("c", null, "9")));

		when(spyProviderForTypeC.getChildren(eq(new Node("c", null, "17")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "19"),
						new Node("c", null, "20")));
		
		when(spyProviderForTypeC.getChildren(eq(new Node("c", null, "18")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "21"),
						new Node("c", null, "22")));
		
		when(spyProviderForTypeC.getChildren(eq(new Node("root", null, "0")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "17"),
						new Node("c", null, "18")));
		
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
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node("b", null, "10")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "23")));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node("b", null, "14")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "24")));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node("c", null, "17")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "25")));
		
		when(spyProviderForTypeFileSystem.getChildren(eq(new Node("c", null, "18")), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "26")));
		// all
		when(spyProviderAll.getChildren(any(Node.class), any(ServiceContext.class)))
				.thenReturn(Arrays.asList(
						new Node("all", null, "0")));
	}
	
	/**
	 * @see <a href=
	 *      "D:\data\java_work\git_repo\flower-platform-4\org.flowerplatform.tests\etc\testTreeStructure.txt"
	 *      >etc\testTreeStructure.txt file</a>
	 */
	@Test
	public void testPriority() {
		assertEquals(nodeService.getChildren(new Node("root", null, "0"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("a", null, "1"),
				new Node("a", null, "2"),
				new Node("b", null, "10"),
				new Node("b", null, "14"),
				new Node("c", null, "17"),
				new Node("c", null, "18"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("a", null, "1"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("a", null, "12"),
				new Node("a", null, "3"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("a", null, "2"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("a", null, "4"),
				new Node("a", null, "5"),
				new Node("b", null, "6"),
				new Node("b", null, "7"),
				new Node("c", null, "8"),
				new Node("c", null, "9"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("b", null, "10"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("b", null, "11"),
				new Node("b", null, "13"),
				new Node("fileSystem", null, "23"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("b", null, "14"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("b", null, "15"),
				new Node("b", null, "16"),
				new Node("fileSystem", null, "24"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("c", null, "17"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("c", null, "19"),
				new Node("c", null, "20"),
				new Node("fileSystem", null, "25"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("c", null, "18"), new ServiceContext<NodeService>(nodeService)), Arrays.asList(
				new Node("c", null, "21"),
				new Node("c", null, "22"),
				new Node("fileSystem", null, "26"),
				new Node("all", null, "0")));
	}
	
}
