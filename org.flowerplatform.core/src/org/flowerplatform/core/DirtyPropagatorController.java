package org.flowerplatform.core;

import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Contains all the common logic for propagation of flags; leaves describing particular behavior up to implementing classes
 * @author Elena Posea
 */
public abstract class DirtyPropagatorController extends AbstractController{

	public abstract void setDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract void unsetDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract void setChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract void unsetChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * @param node
	 *            the node which I want to test for dirty
	 * @param serviceContext
	 *            the service used for this node
	 * @return true, if the node isDisrty, accordingly to the logic of being
	 *         dirty in the implementing class false, otherwise
	 */
	public abstract boolean isDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract boolean isChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * @param node
	 *            the node on which I want to start propagation
	 * @param serviceContext
	 *            the service used for this node
	 */
	public void setDirtyAndPropagateToParents(Node node, ServiceContext<NodeService> serviceContext) {
		if (isDirty(node, serviceContext)){
			return;
		}
		setDirty(node, serviceContext);
		// propagate childrenDirty flag for parents
		Node parent = null;
		while ((parent = serviceContext.getService().getParent(node, serviceContext)) != null) {
			if (isChildrenDirty(parent, serviceContext)) {
				// the parentSync flag has already been propagated
				return;
			}
			// set childrenDirty to default value for is dirty / mark
			setChildrenDirty(parent, serviceContext);
			node = parent;
		}
	}

	public void unsetDirtyAndPropagateToParents(Node node, ServiceContext<NodeService> serviceContext) {
		if (!isDirty(node, serviceContext) && !isChildrenDirty(node, serviceContext)) {
			// already set
			return;
		}
		// set dirty false/ it is not dirty
		unsetDirty(node, serviceContext);
		
		// propagate childrenDirty flag for parents
		Node parent = null;
		while ((parent = serviceContext.getService().getParent(node, serviceContext)) != null) {
			if (!isChildrenDirty(parent, serviceContext)) {
				// the parentSync flag has already been propagated
				return;
			}
			// besides this dirtyChild that I just cleaned up, is there any other dirtyChildren?
			if (containsDirtyChild(parent, serviceContext)) {
				return;
			}
			// set childrenDirty (flag2) to default value for is dirty / mark
			unsetChildrenDirty(parent, serviceContext);
			node = parent;
		}
	}

	private boolean containsDirtyChild(Node node, ServiceContext<NodeService> serviceContext) {
		NodeService service = serviceContext.getService();
		List<Node> nodeList = service.getChildren(node, new ServiceContext<NodeService>(service).add(POPULATE_WITH_PROPERTIES, true)); 
		for (Node child : nodeList) {
			if ((isDirty(child, serviceContext) || isChildrenDirty(child, serviceContext))) {
				return true;
			}
		}
		return false;
	}

}
