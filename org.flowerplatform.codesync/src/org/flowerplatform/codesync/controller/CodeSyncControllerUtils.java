package org.flowerplatform.codesync.controller;


import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.ADDED;
import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.CHILDREN_SYNC;
import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.CONFLICT;
import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.REMOVED;
import static org.flowerplatform.codesync.CodeSyncPropertiesConstants.SYNC;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncControllerUtils {

	public static final String ORIGINAL_SUFFIX = ".original";
	public static final String CONFLICT_SUFFIX = ".conflict";
	
	public static boolean isOriginalPropertyName(String property) {
		return property.endsWith(ORIGINAL_SUFFIX);
	}
	
	public static String getOriginalPropertyName(String property) {
		return property + ORIGINAL_SUFFIX;
	}
	
	public static boolean isConflictPropertyName(String property) {
		return property.endsWith(CONFLICT_SUFFIX);
	}
	
	public static String getConflictPropertyName(String property) {
		return property + CONFLICT_SUFFIX;
	}
	
	public static boolean isCodeSyncFlagConstant(String property) {
		if (SYNC.equals(property) || CHILDREN_SYNC.equals(property) || 
				CONFLICT.equals(property) || CHILDREN_CONFLICT.equals(property) ||
				ADDED.equals(property) || REMOVED.equals(property)) {
			return true;
		}
		return false;
	}
	
	public static void setSyncFalseAndPropagateToParents(Node node, NodeService service) {
		// set sync false
		service.setProperty(node, SYNC, false);
		
		// propagate childrenSync flag for parents
		Node parent = null;
		while ((parent = service.getParent(node)) != null) {
			if (!isChildrenSync(parent)) {
				// the parentSync flag has already been propagated
				return;
			}
			// set childrenSync false
			service.setProperty(parent, CHILDREN_SYNC, false);
			node = parent;
		}
	}
	
	public static void setSyncTrueAndPropagateToParents(Node node, NodeService service) {
		if (isSync(node)) {
			// already set
			return;
		}
		
		// added/removed nodes are not sync
		if (isAdded(node) || isRemoved(node)) {
			return;
		}
		
		// check if it has any property.original set
		boolean sync = true;
		for (String property : node.getOrPopulateProperties().keySet()) {
			if (isOriginalPropertyName(property)) {
				sync = false;
			}
		}
		if (!sync) {
			return;
		}
		
		// set sync true
		service.setProperty(node, SYNC, true);
		
		// propagate childrenSync flag for parents
		setChildrenSyncTrueAndPropagateToParents(service.getParent(node), service);
	}
	
	public static void setChildrenSyncTrueAndPropagateToParents(Node parent, NodeService service) {
		while (parent != null) {
			// if childrenSync is already true for the parent, no need to go up
			if (isChildrenSync(parent)) {
				return;
			}
			
			if (!allChildrenSync(parent, service)) {
				return;
			}
			
			// set childrenSync true
			service.setProperty(parent, CHILDREN_SYNC, true);
			
			// if this parent is not sync, then its parents' childrenSync flag can't be set to true
			// it's better to just stop now
			if (!isSync(parent)) {
				return;
			}
			
			parent = service.getParent(parent);
		}
	}
	
	public static void setConflictTrueAndPropagateToParents(Node node, String conflictProperty, Object conflictValue, NodeService service) {
		service.setProperty(node, getConflictPropertyName(conflictProperty), conflictValue);
		
		if (isConflict(node)) {
			// already set
			return;
		}
		
		// set conflict true
		service.setProperty(node, CONFLICT, true);
		
		// propagate childrenConflict flag for parents
		Node parent = null;
		while ((parent = service.getParent(node)) != null) {
			if (isChildrenConflict(parent)) {
				// the childrenConflict flag has already been propagated
				return;
			}
			// set childrenConflict false
			service.setProperty(parent, CHILDREN_CONFLICT, true);
			node = parent;
		}
	}

	public static void setConflictFalseAndPropagateToParents(Node node, String conflictProperty, NodeService service) {
		service.unsetProperty(node, getConflictPropertyName(conflictProperty));
		
		if (!isConflict(node)) {
			return;
		}
		
		// check if it has any other property.conflict set
		boolean conflict = false;
		for (String property : node.getOrPopulateProperties().keySet()) {
			if (isConflictPropertyName(property)) {
				conflict = true;
			}
		}
		if (conflict) {
			return;
		}
		
		// set conflict false
		service.setProperty(node, CONFLICT, false);
		if (noChildConflict(node, service)) {
			service.setProperty(node, CHILDREN_CONFLICT, false);
		}
		
		// propagate childrenConflict flag for parents
		Node parent = null;
		while ((parent = service.getParent(node)) != null) {
			if (!noChildConflict(parent, service)) {
				return;
			}
			// if childrenConflict is already true for the parent, no need to go up
			if (!isChildrenConflict(parent)) {
				return;
			}
			// set childrenConflict false
			service.setProperty(parent, CHILDREN_CONFLICT, false);
			node = parent;
		}
	}
	
	public static boolean allChildrenSync(Node node, NodeService service) {
		for (Node child : service.getChildren(node, false)) {
			if (!isSync(child) || isAdded(child) || isRemoved(child)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean noChildConflict(Node node, NodeService service) {
		for (Node child : service.getChildren(node, true)) {
			if (isConflict(child) || isChildrenConflict(child)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isSync(Node node) {
		return hasFlagTrue(node, SYNC);
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
	
	public static boolean isConflict(Node node) {
		return hasFlagTrue(node, CONFLICT);
	}
	
	public static boolean isChildrenConflict(Node node) {
		return hasFlagTrue(node, CHILDREN_CONFLICT);
	}
	
	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getOrPopulateProperties().get(flag);
		return b != null && b;
	}
}
