package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.CodeSyncPlugin.ADDED_MARKER;
import static org.flowerplatform.codesync.CodeSyncPlugin.CHILDREN_CONFLICT_MARKER;
import static org.flowerplatform.codesync.CodeSyncPlugin.CHILDREN_SYNC_MARKER;
import static org.flowerplatform.codesync.CodeSyncPlugin.CONFLICT_MARKER;
import static org.flowerplatform.codesync.CodeSyncPlugin.REMOVED_MARKER;
import static org.flowerplatform.codesync.CodeSyncPlugin.SYNC_MARKER;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncControllerUtils {

	public static final String ORIGINAL = ".original";
	public static final String CONFLICT = ".conflict";
	
	public static boolean isOriginalPropertyName(String property) {
		return property.endsWith(ORIGINAL);
	}
	
	public static String getOriginalPropertyName(String property) {
		return property + ORIGINAL;
	}
	
	public static boolean isConflictPropertyName(String property) {
		return property.endsWith(CONFLICT);
	}
	
	public static String getConflictPropertyName(String property) {
		return property + CONFLICT;
	}
	
	public static boolean isCodeSyncFlagConstant(String property) {
		if (SYNC_MARKER.equals(property) || CHILDREN_SYNC_MARKER.equals(property) || 
				CONFLICT_MARKER.equals(property) || CHILDREN_CONFLICT_MARKER.equals(property) ||
				ADDED_MARKER.equals(property) || REMOVED_MARKER.equals(property)) {
			return true;
		}
		return false;
	}
	
	public static void setSyncFalseAndPropagateToParents(Node node, NodeService service) {
		// set sync false
		service.setProperty(node, SYNC_MARKER, false);
		
		// propagate childrenSync flag for parents
		Node parent = null;
		while ((parent = service.getParent(node)) != null) {
			if (!isChildrenSync(parent)) {
				// the parentSync flag has already been propagated
				return;
			}
			// set childrenSync false
			service.setProperty(parent, CHILDREN_SYNC_MARKER, false);
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
		service.setProperty(node, SYNC_MARKER, true);
		
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
			service.setProperty(parent, CHILDREN_SYNC_MARKER, true);
			
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
		service.setProperty(node, CONFLICT_MARKER, true);
		
		// propagate childrenConflict flag for parents
		Node parent = null;
		while ((parent = service.getParent(node)) != null) {
			if (isChildrenConflict(parent)) {
				// the childrenConflict flag has already been propagated
				return;
			}
			// set childrenConflict false
			service.setProperty(parent, CHILDREN_CONFLICT_MARKER, true);
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
		service.setProperty(node, CONFLICT_MARKER, false);
		if (noChildConflict(node, service)) {
			service.setProperty(node, CHILDREN_CONFLICT_MARKER, false);
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
			service.setProperty(parent, CHILDREN_CONFLICT_MARKER, false);
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
		return hasFlagTrue(node, SYNC_MARKER);
	}
	
	public static boolean isChildrenSync(Node node) {
		return hasFlagTrue(node, CHILDREN_SYNC_MARKER);
	}
	
	public static boolean isAdded(Node node) {
		return hasFlagTrue(node, ADDED_MARKER);
	}
	
	public static boolean isRemoved(Node node) {
		return hasFlagTrue(node, REMOVED_MARKER);
	}
	
	public static boolean isConflict(Node node) {
		return hasFlagTrue(node, CONFLICT_MARKER);
	}
	
	public static boolean isChildrenConflict(Node node) {
		return hasFlagTrue(node, CHILDREN_CONFLICT_MARKER);
	}
	
	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getOrPopulateProperties().get(flag);
		return b != null && b;
	}
}
