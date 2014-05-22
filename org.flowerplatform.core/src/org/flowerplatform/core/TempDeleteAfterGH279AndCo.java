package org.flowerplatform.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.AddNodeController;
import org.flowerplatform.core.node.controller.ChildrenProvider;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

public class TempDeleteAfterGH279AndCo {

	public static final TempDeleteAfterGH279AndCo INSTANCE = new TempDeleteAfterGH279AndCo(); 
	
	public void init() {
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor("commandStackDummyRoot1")
		.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "commandStackDummyRoot1"))
		.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ChildrenProvider() {
			
			@Override
			public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
				return true;
			}
			
			@Override
			public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
				return Collections.singletonList(new Node("commandStack1", "self", node.getIdWithinResource(), null));
			}
		});
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor("commandStack1")
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "commandStack1"))
			.addAdditiveController(CoreConstants.PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.IS_SUBSCRIBABLE, true))
			.addAdditiveController(CoreConstants.CHILDREN_PROVIDER, new ChildrenProvider() {
				
				@Override
				public boolean hasChildren(Node node, ServiceContext<NodeService> context) {
					return true;
				}
				
				@Override
				public List<Node> getChildren(Node node, ServiceContext<NodeService> context) {
					return tempCommandStackList;
				}
			})
			.addAdditiveController(CoreConstants.ADD_NODE_CONTROLLER, new AddNodeController() {
				
				@Override
				public void addNode(Node node, Node child,
						ServiceContext<NodeService> context) {
					tempCommandStackList.add(child);					
				}
			});
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor("command1");

	}
	
	public List<Node> tempCommandStackList = new ArrayList<>();
	
	public int counter;
	
	public Node createCommandNode(String label) {
		Node node;
		
		node = new Node("command1", null, Integer.toString(counter++), null);
		if (label == null) {
			label = node.getIdWithinResource();
		}
		node.getProperties().put("name", label);
		
		return node;
	}
	
	public void addNewNode() {
		Node commandStackNode = new Node("commandStack1", "self", null, null);
		Node childNode = createCommandNode(null);
		CorePlugin.getInstance().getNodeService().addChild(commandStackNode, childNode, new ServiceContext<NodeService>());
	}
	
	public TempDeleteAfterGH279AndCo() {
		tempCommandStackList.add(createCommandNode("command 1"));
		tempCommandStackList.add(createCommandNode("command 2"));
	}


}
