package org.flowerplatform.codesync.sdiff.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.internal.signedcontent.LegacyVerifierFactory;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IChildrenProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.LEGEND;

/** 
 * 
 * @author Tita Andreea
 *
 */

public class StructureDiffLegendProvider extends AbstractController implements IChildrenProvider{

	public StructureDiffLegendProvider(){
		setOrderIndex(-10000);
	} 
	
	public List<Node> getChildren(Node node, ServiceContext<NodeService> context){
		List<Node> children = new ArrayList<Node>();
		//add uri to Legend node
		children.add(new Node("legend:legend",LEGEND));
		return children;
	}
	
	
	public boolean hasChildren(Node node, ServiceContext<NodeService> context){
		return true;
	}
	
}
