package org.flowerplatform.core;

import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.List;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * 
 * @author Elena Posea
 */
public abstract class DirtyPropagatorController extends AbstractController{

	public abstract void setDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract void unsetDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract void setChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract void unsetChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * In Node node, for property flagProperty, is it changer, or does it have
	 * the default value?
	 * 
	 * @param node
	 * @param flagProperty
	 * @param defaultFlagValue
	 * @return true, if the flagProperty property of node is different than the
	 *         defaultFlagValue
	 */
	public abstract boolean isDirty(Node node, ServiceContext<NodeService> serviceContext);

	public abstract boolean isChildrenDirty(Node node, ServiceContext<NodeService> serviceContext);

	/**
	 * @param node
	 *            the node on which I want to start propagation
	 * @param serviceContext
	 *            the service used for this node
	 * @param propagator
	 *            the class that contains the implementation for
	 */
	public void setDirtyAndPropagateToParents(Node node, ServiceContext<NodeService> serviceContext) {
		if(isDirty(node, serviceContext)){
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
			// set childrenDirty (flag2) to default value for is dirty / mark
			setChildrenDirty(parent, serviceContext);
			// mark it as having dirty children true
			// service.setProperty(parent, flag2, defaultValueForIsDirty, new
			// ServiceContext<NodeService>(service));
			node = parent;
		}
	}

	public void unsetDirtyAndPropagateToParents(Node node, ServiceContext<NodeService> serviceContext) {
		if (!isDirty(node, serviceContext) && !isChildrenDirty(node, serviceContext)) {
			// already set
			return;
		}
		// set DIRTY false/ it is not dirty
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
