package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.CodeSyncPlugin.ADDED;
import static org.flowerplatform.codesync.CodeSyncPlugin.CATEGORY;
import static org.flowerplatform.codesync.CodeSyncPlugin.CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncPlugin.CHILDREN_SYNC;
import static org.flowerplatform.codesync.CodeSyncPlugin.CONFLICT;
import static org.flowerplatform.codesync.CodeSyncPlugin.REMOVED;
import static org.flowerplatform.codesync.CodeSyncPlugin.SYNC;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncControllerUtils {

	public static final String ORIGINAL = ".original";
	
	public static boolean isOriginalPropertyName(String property) {
		return property.endsWith(ORIGINAL);
	}
	
	public static String getOriginalPropertyName(String property) {
		return property + ORIGINAL;
	}
	
	public static boolean isCodeSyncFlagConstant(String property) {
		if (property.equals(SYNC) || property.equals(CHILDREN_SYNC) || 
				property.equals(CONFLICT) || property.equals(CHILDREN_CONFLICT) ||
				property.equals(ADDED) || property.equals(REMOVED)) {
			return true;
		}
		return false;
	}
	
	public static void setSyncFalseAndPropagateParentSyncFalse(Node node, NodeService service) {
		// set sync false
		service.setProperty(node, SYNC, false);
		
		// propagate childrenSync flag for parents
		Node parent = null;
		while ((parent = service.getParent(node, true)) != null) {
			// if the parent was marked not sync, then the parentSync flag has already been propagated
			if (!isSync(parent) || !isChildrenSync(parent)) {
				return;
			}
			// set childrenSync false
			service.setProperty(parent, CHILDREN_SYNC, false);
			node = parent;
		}
	}
	
	public static void setSyncTrueAndPropagateParentSyncTrue(Node node, NodeService service) {
		// added/removed nodes are not sync
		if (isAdded(node) || isRemoved(node)) {
			return;
		}
		
		// check if it has any property.original set
		boolean sync = true;
		for (String property : node.getOrCreateProperties().keySet()) {
			if (isOriginalPropertyName(property)) {
				sync = false;
			}
		}
		if (!sync) {
			return;
		}
		
		// set sync true
		service.setProperty(node, SYNC, true);
		if (allChildrenSync(node, service)) {
			service.setProperty(node, CHILDREN_SYNC, true);
		}
		
		// propagate childrenSync flag for parents
		Node parent = null;
		while ((parent = service.getParent(node, true)) != null) {
			if (!allChildrenSync(parent, service)) {
				return;
			}
			// if childrenSync is already true for the parent, no need to go up
			if (isChildrenSync(parent)) {
				return;
			}
			// set childrenSync true
			service.setProperty(parent, CHILDREN_SYNC, true);
			node = parent;
		}
	}
	
	public static boolean allChildrenSync(Node node, NodeService service) {
		for (Node child : service.getChildren(node, true)) {
			if (!isSync(child) || !isChildrenSync(child) || isAdded(child) || isRemoved(child)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isCategoryNode(Node node) {
		return CATEGORY.equals(node.getType());
	}
	
	public static boolean isSync(Node node) {
		// category nodes are always sync
		return isCategoryNode(node) || hasFlagTrue(node, SYNC);
	}
	
	public static boolean isChildrenSync(Node node) {
		return hasFlagTrue(node, CHILDREN_SYNC);
	}
	
	public static boolean isAdded(Node node) {
		return hasFlagTrue(node, ADDED);
	}
	
	public static boolean isRemoved(Node node) {
		return hasFlagTrue(node, REMOVED);
	}
	
	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getOrCreateProperties().get(flag);
		return b != null && b;
	}
}
