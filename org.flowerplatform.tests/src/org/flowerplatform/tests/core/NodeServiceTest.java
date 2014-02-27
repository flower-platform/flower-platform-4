package org.flowerplatform.tests.core;

import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sebastian Solomon
 */
public class NodeServiceTest {
	private static NodeService nodeService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		class MockChildProvider extends ChildrenProvider {
			public List<Node> getChildren(Node node) {
				return null;
			}
		}
		
		ChildrenProvider spyProviderForTypeA = spy(new MockChildProvider());
		ChildrenProvider spyProviderForTypeB = spy(new MockChildProvider());
		ChildrenProvider spyProviderForTypeC = spy(new MockChildProvider());
		ChildrenProvider spyProviderForTypeFileSystem = spy(new MockChildProvider());
		ChildrenProvider spyProviderAll = spy(new MockChildProvider());
		
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
						   
		
		when(spyProviderForTypeA.getChildren(new Node("root", null, "0", null)))
				.thenReturn(Arrays.asList(
						new Node("a", null, "1", null),
						new Node("a", null, "2", null)));
		
		when(spyProviderForTypeA.getChildren(new Node("a", null, "1", null)))
				.thenReturn(Arrays.asList(
						new Node("a", null, "12", null),
						new Node("a", null, "3", null)));

		when(spyProviderForTypeA.getChildren(new Node("a", null, "2", null)))
				.thenReturn(Arrays.asList(
						new Node("a", null, "4", null),
						new Node("a", null, "5", null)));

		// B
		TypeDescriptor nodeTypeDescriptorB = descriptorRegistry
				.getOrCreateTypeDescriptor("b");
		
		nodeTypeDescriptorB.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll);
		nodeTypeDescriptorB.addCategory("category.fileSystem");
		
		when(spyProviderForTypeB.getChildren(new Node("a", null, "2", null)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "6", null),
						new Node("b", null, "7", null)));

		when(spyProviderForTypeB.getChildren(new Node("b", null, "10", null)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "11", null),
						new Node("b", null, "13", null)));
		
		when(spyProviderForTypeB.getChildren(new Node("b", null, "14", null)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "15", null),
						new Node("b", null, "16", null)));
		
		when(spyProviderForTypeB.getChildren(new Node("root", null, "0", null)))
				.thenReturn(Arrays.asList(
						new Node("b", null, "10", null),
						new Node("b", null, "14", null)));
		
		// C
		TypeDescriptor nodeTypeDescriptorC = descriptorRegistry
				.getOrCreateTypeDescriptor("c");
		nodeTypeDescriptorC.addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeC)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeA)
						   .addAdditiveController(CHILDREN_PROVIDER, spyProviderForTypeB)
		   				   .addAdditiveController(CHILDREN_PROVIDER, spyProviderAll)
		   				   .addCategory("category.fileSystem");
		
		when(spyProviderForTypeC.getChildren(new Node("a", null, "2", null)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "8", null),
						new Node("c", null, "9", null)));

		when(spyProviderForTypeC.getChildren(new Node("c", null, "17", null)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "19", null),
						new Node("c", null, "20", null)));
		
		when(spyProviderForTypeC.getChildren(new Node("c", null, "18", null)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "21", null),
						new Node("c", null, "22", null)));
		
		when(spyProviderForTypeC.getChildren(new Node("root", null, "0", null)))
				.thenReturn(Arrays.asList(
						new Node("c", null, "17", null),
						new Node("c", null, "18", null)));
		
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
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("b", null, "10", null)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "23", null)));
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("b", null, "14", null)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "24", null)));
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("c", null, "17", null)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "25", null)));
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("c", null, "18", null)))
				.thenReturn(Arrays.asList(
						new Node("fileSystem", null, "26", null)));
		// all
		when(spyProviderAll.getChildren(any(Node.class)))
				.thenReturn(Arrays.asList(
						new Node("all", null, "0", null)));
	}
	
	/**
	 * @see <a href=
	 *      "D:\data\java_work\git_repo\flower-platform-4\org.flowerplatform.tests\etc\testTreeStructure.txt"
	 *      >etc\testTreeStructure.txt file</a>
	 */
	@Test
	public void testPriority() {
		assertEquals(nodeService.getChildren(new Node("root", null, "0", null), false), Arrays.asList(
				new Node("a", null, "1", null),
				new Node("a", null, "2", null),
				new Node("b", null, "10", null),
				new Node("b", null, "14", null),
				new Node("c", null, "17", null),
				new Node("c", null, "18", null),
				new Node("all", null, "0", null)));
		
		assertEquals(nodeService.getChildren(new Node("a", null, "1", null), false), Arrays.asList(
				new Node("a", null, "12", null),
				new Node("a", null, "3", null),
				new Node("all", null, "0", null)));
		
		assertEquals(nodeService.getChildren(new Node("a", null, "2", null), false), Arrays.asList(
				new Node("a", null, "4", null),
				new Node("a", null, "5", null),
				new Node("b", null, "6", null),
				new Node("b", null, "7", null),
				new Node("c", null, "8", null),
				new Node("c", null, "9", null),
				new Node("all", null, "0", null)));
		
		assertEquals(nodeService.getChildren(new Node("b", null, "10", null), false), Arrays.asList(
				new Node("b", null, "11", null),
				new Node("b", null, "13", null),
				new Node("fileSystem", null, "23", null),
				new Node("all", null, "0", null)));
		
		assertEquals(nodeService.getChildren(new Node("b", null, "14", null), false), Arrays.asList(
				new Node("b", null, "15", null),
				new Node("b", null, "16", null),
				new Node("fileSystem", null, "24", null),
				new Node("all", null, "0", null)));
		
		assertEquals(nodeService.getChildren(new Node("c", null, "17", null), false), Arrays.asList(
				new Node("c", null, "19", null),
				new Node("c", null, "20", null),
				new Node("fileSystem", null, "25", null),
				new Node("all", null, "0", null)));
		
		assertEquals(nodeService.getChildren(new Node("c", null, "18", null), false), Arrays.asList(
				new Node("c", null, "21", null),
				new Node("c", null, "22", null),
				new Node("fileSystem", null, "26", null),
				new Node("all", null, "0", null)));
	}
	
}
