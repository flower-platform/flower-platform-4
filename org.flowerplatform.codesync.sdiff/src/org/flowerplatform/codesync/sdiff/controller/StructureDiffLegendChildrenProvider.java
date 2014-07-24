package org.flowerplatform.codesync.sdiff.controller;

import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND_CHILD;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND_CHILD_NB;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/** 
 * 
 * @author Tita Andreea
 *
 */

public class StructureDiffLegendChildrenProvider extends AbstractController implements IChildrenProvider{

	public StructureDiffLegendChildrenProvider(){
		setOrderIndex(-10000);
	} 
	
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context){
		List<Node> children = new ArrayList<Node>();
		
		for(int i = 0; i < LEGEND_CHILD_NB; i++){
			children.add(new Node("legendChild:legendChild" + Integer.toString(i),LEGEND_CHILD));
		}
		
		return children;
	}
	
	
	public boolean hasChildren(Node node, ServiceContext<NodeService> context){
		return true;
	}
	
}