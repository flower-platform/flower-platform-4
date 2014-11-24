package org.flowerplatform.core;

import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * Contains all the common logic for propagation of flags; leaves describing particular behavior up to implementing classes.
 * 
 * @author Elena Posea
 * @author Mariana Gheorghe
 */
public abstract class DirtyPropagatorController extends AbstractController {

	/**
	 * Set the "dirty" property for this node, as specified in the extending class.
	 */
	public abstract void setDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * Unset the "dirty" property for this node, as specified in the extending class.
	 */
	public abstract void unsetDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * Set the "children dirty" property for this node, as specified in the extending class.
	 */
	public abstract void setChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * Unset the "children dirty" property for this node, as specified in the extending class.
	 */
	public abstract void unsetChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * Return the value of the "dirty" property for this node, as specified in the extending class.
	 */
	public abstract boolean isDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * Return the value of the "children dirty" property for this node, as specified in the extending class.
	 */
	public abstract boolean isChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * Set the "dirty" property for this node, and propagate the "children dirty" property to this node's parents.
	 */
	public void setDirtyAndPropagateToParents(Node node, ServiceContext<NodeService> serviceContext) {
		if (isDirty(node, serviceContext)) {
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

	/**
	 * Unset the "dirty" property for this node, and propagate the "children dirty" property to this node's parents.
	 */
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
			// besides this dirtyChild that I just cleaned up, are there any other dirtyChildren?
			if (containsDirtyChild(parent, serviceContext)) {
				return;
			}
			// set childrenDirty to default value for is dirty / mark
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
