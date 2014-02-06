package org.flowerplatform.core.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.core.node.NodeTypeDescriptor;
import org.flowerplatform.core.node.NodeTypeDescriptorRegistry;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.util.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sebastian Solomon
 */
public class NodeServiceTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testPriority() {
		
		class MockChildProvider extends ChildrenProvider {
			public List<Pair<Node, Object>> getChildren(Node node) {
				return null;
			}
		}
		
		ChildrenProvider spyProviderForTypeA = spy(new MockChildProvider());
		ChildrenProvider spyProviderForTypeB = spy(new MockChildProvider());
		ChildrenProvider spyProviderForTypeC = spy(new MockChildProvider());
		ChildrenProvider spyProviderForTypeFileSystem = spy(new MockChildProvider());
		ChildrenProvider spyProviderAll = spy(new MockChildProvider());
		
		// setPrioritys
		spyProviderForTypeA.setPriority(-100);
		spyProviderForTypeB.setPriority(0);
		spyProviderForTypeC.setPriority(100);
		spyProviderForTypeFileSystem.setPriority(200);
		spyProviderAll.setPriority(Integer.MAX_VALUE);
		
		NodeTypeDescriptorRegistry descriptorRegistry = new NodeTypeDescriptorRegistry();
		NodeService nodeService = new NodeService(descriptorRegistry);
		
		// A
		NodeTypeDescriptor nodeTypeDescriptorA = descriptorRegistry
				.getOrCreateNodeTypeDescriptor("a");
		nodeTypeDescriptorA.addChildrenProvider(spyProviderForTypeA);
		nodeTypeDescriptorA.addChildrenProvider(spyProviderForTypeC);
		nodeTypeDescriptorA.addChildrenProvider(spyProviderForTypeB);
		nodeTypeDescriptorA.addChildrenProvider(spyProviderAll);
		
		when(spyProviderForTypeA.getChildren(new Node("a", null, "1")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("a", null, "12"), null),
						new Pair<Node, Object>(new Node("a", null, "3"), null)));

		when(spyProviderForTypeA.getChildren(new Node("a", null, "2")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("a", null, "4"), null),
						new Pair<Node, Object>(new Node("a", null, "5"), null)));

		when(spyProviderForTypeA.getChildren(new Node("root", null, "0")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("a", null, "1"), null),
						new Pair<Node, Object>(new Node("a", null, "2"), null)));
		// B
		NodeTypeDescriptor nodeTypeDescriptorB = descriptorRegistry
				.getOrCreateNodeTypeDescriptor("b");
		nodeTypeDescriptorB.addChildrenProvider(spyProviderForTypeB);
		nodeTypeDescriptorB.addChildrenProvider(spyProviderAll);
		nodeTypeDescriptorB.addCategory("category.fileSystem");
		
		when(spyProviderForTypeB.getChildren(new Node("a", null, "2")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("b", null, "6"), null),
						new Pair<Node, Object>(new Node("b", null, "7"), null)));

		when(spyProviderForTypeB.getChildren(new Node("b", null, "10")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("b", null, "11"), null),
						new Pair<Node, Object>(new Node("b", null, "13"), null)));
		
		when(spyProviderForTypeB.getChildren(new Node("b", null, "14")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("b", null, "15"), null),
						new Pair<Node, Object>(new Node("b", null, "16"), null)));
		
		when(spyProviderForTypeB.getChildren(new Node("root", null, "0")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("b", null, "10"), null),
						new Pair<Node, Object>(new Node("b", null, "14"), null)));
		
		// C
		NodeTypeDescriptor nodeTypeDescriptorC = descriptorRegistry
				.getOrCreateNodeTypeDescriptor("c");
		nodeTypeDescriptorC.addChildrenProvider(spyProviderForTypeC);
		nodeTypeDescriptorC.addChildrenProvider(spyProviderForTypeA);
		nodeTypeDescriptorC.addChildrenProvider(spyProviderForTypeB);
		nodeTypeDescriptorC.addChildrenProvider(spyProviderAll);
		nodeTypeDescriptorC.addCategory("category.fileSystem");
		
		when(spyProviderForTypeC.getChildren(new Node("a", null, "2")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("c", null, "8"), null),
						new Pair<Node, Object>(new Node("c", null, "9"), null)));

		when(spyProviderForTypeC.getChildren(new Node("c", null, "17")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("c", null, "19"), null),
						new Pair<Node, Object>(new Node("c", null, "20"), null)));
		
		when(spyProviderForTypeC.getChildren(new Node("c", null, "18")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("c", null, "21"), null),
						new Pair<Node, Object>(new Node("c", null, "22"), null)));
		
		when(spyProviderForTypeC.getChildren(new Node("root", null, "0")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("c", null, "17"), null),
						new Pair<Node, Object>(new Node("c", null, "18"), null)));
		
		// root
		NodeTypeDescriptor nodeTypeDescriptorRoot = descriptorRegistry
				.getOrCreateNodeTypeDescriptor("root");
		nodeTypeDescriptorRoot.addChildrenProvider(spyProviderAll);
		nodeTypeDescriptorRoot.addChildrenProvider(spyProviderForTypeA);
		nodeTypeDescriptorRoot.addChildrenProvider(spyProviderForTypeB);
		nodeTypeDescriptorRoot.addChildrenProvider(spyProviderForTypeC);
		
		// fileSystem
		NodeTypeDescriptor fileSystemTypeDescriptor = descriptorRegistry
				.getOrCreateNodeTypeDescriptor("category.fileSystem");
		fileSystemTypeDescriptor.addChildrenProvider(spyProviderForTypeFileSystem);
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("b", null, "10")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("fileSystem", null, "23"), null)));
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("b", null, "14")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("fileSystem", null, "24"), null)));
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("c", null, "17")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("fileSystem", null, "25"), null)));
		
		when(spyProviderForTypeFileSystem.getChildren(new Node("c", null, "18")))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("fileSystem", null, "26"), null)));
		// all
		when(spyProviderAll.getChildren(any(Node.class)))
				.thenReturn(Arrays.asList(
						new Pair<Node, Object>(new Node("all", null, "0"), null)));
		
		assertEquals(nodeService.getChildren(new Node("root", null, "0"), false), Arrays.asList(
				new Node("a", null, "1"),
				new Node("a", null, "2"),
				new Node("b", null, "10"),
				new Node("b", null, "14"),
				new Node("c", null, "17"),
				new Node("c", null, "18"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("a", null, "1"), false), Arrays.asList(
				new Node("a", null, "12"),
				new Node("a", null, "3"),
				new Node("all", null, "0")));
					
		
		assertEquals(nodeService.getChildren(new Node("a", null, "2"), false), Arrays.asList(
				new Node("a", null, "4"),
				new Node("a", null, "5"),
				new Node("b", null, "6"),
				new Node("b", null, "7"),
				new Node("c", null, "8"),
				new Node("c", null, "9"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("b", null, "10"), false), Arrays.asList(
				new Node("b", null, "11"),
				new Node("b", null, "13"),
				new Node("fileSystem", null, "23"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("b", null, "14"), false), Arrays.asList(
				new Node("b", null, "15"),
				new Node("b", null, "16"),
				new Node("fileSystem", null, "24"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("c", null, "17"), false), Arrays.asList(
				new Node("c", null, "19"),
				new Node("c", null, "20"),
				new Node("fileSystem", null, "25"),
				new Node("all", null, "0")));
		
		assertEquals(nodeService.getChildren(new Node("c", null, "18"), false), Arrays.asList(
				new Node("c", null, "21"),
				new Node("c", null, "22"),
				new Node("fileSystem", null, "26"),
				new Node("all", null, "0")));
	}
	
}
