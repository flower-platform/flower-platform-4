/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.codesync.controller;

import static org.flowerplatform.codesync.CodeSyncConstants.ADDED;
import static org.flowerplatform.codesync.CodeSyncConstants.CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.CHILDREN_SYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.ORIGINAL_SUFFIX;
import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
import static org.flowerplatform.codesync.CodeSyncConstants.SYNC;

import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public final class CodeSyncControllerUtils {
	
	private CodeSyncControllerUtils() {
	}

//	public static boolean isOriginalPropertyName(String property) {
//		return property.endsWith(ORIGINAL_SUFFIX);
//	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static String getOriginalPropertyName(String property) {
		return property + ORIGINAL_SUFFIX;
	}
	
//	public static boolean isConflictPropertyName(String property) {
//		return property.endsWith(CONFLICT_SUFFIX);
//	}
	
//	public static String getConflictPropertyName(String property) {
//		return property + CONFLICT_SUFFIX;
//	}
	
	/**
	 *@author Cristian Spiescu
	 **/
	public static boolean isCodeSyncFlagConstant(String property) {
		if (SYNC.equals(property) || CHILDREN_SYNC.equals(property) 
				|| CONFLICT.equals(property) || CHILDREN_CONFLICT.equals(property) 
				|| ADDED.equals(property) || REMOVED.equals(property)) {
			return true;
		}
		return false;
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static void setSyncFalseAndPropagateToParents(Node node, NodeService service) {		
		// set sync false
		service.setProperty(node, SYNC, false, new ServiceContext<NodeService>(service));
		
		// propagate childrenSync flag for parents
		Node parent = null;
		while ((parent = service.getParent(node, new ServiceContext<NodeService>(service))) != null) {
			if (!isChildrenSync(parent)) {
				// the parentSync flag has already been propagated
				return;
			}
			// set childrenSync false
			service.setProperty(parent, CHILDREN_SYNC, false, new ServiceContext<NodeService>(service));
			node = parent;
		}
	}
	
	/**
	 *@author Valentina Bojan
	 **/
	public static void setSyncTrueAndPropagateToParents(Node node, NodeService service) {		
//		if (isSync(node)) {
//			// already set
//			return;
//		}
//		
//		// added/removed nodes are not sync
//		if (isAdded(node) || isRemoved(node)) {
//			return;
//		}
//		
//		// check if it has any property.original set
//		boolean sync = true;
//		for (String property : node.getProperties().keySet()) {
//			if (isOriginalPropertyName(property)) {
//				sync = false;
//			}
//		}
//		if (!sync) {
//			return;
//		}
//		
//		// set sync true
//		service.setProperty(node, SYNC, true, new ServiceContext<NodeService>(service));
//		
//		 propagate childrenSync flag for parents
//		setChildrenSyncTrueAndPropagateToParents(service.getParent(node, new ServiceContext<NodeService>(service)), service);
	}
	
	/**
	 *@author Valentina Bojan
	 **/
	public static void setChildrenSyncTrueAndPropagateToParents(Node parent, NodeService service) {	
//		while (parent != null) {
//			// if childrenSync is already true for the parent, no need to go up
//			if (isChildrenSync(parent)) {
//				return;
//			}
//			
//			if (!allChildrenSync(parent, service)) {
//				return;
//			}
//			
//			// set childrenSync true
//			service.setProperty(parent, CHILDREN_SYNC, true, new ServiceContext<NodeService>(service));
//			
//			// if this parent is not sync, then its parents' childrenSync flag can't be set to true
//			// it's better to just stop now
//			if (!isSync(parent)) {
//				return;
//			}
//			
//			parent = service.getParent(parent, new ServiceContext<NodeService>(service));
//		}
	}
//	
//	public static void setConflictTrueAndPropagateToParents(Node node, String conflictProperty, Object conflictValue, NodeService service) {		
//		service.setProperty(node, getConflictPropertyName(conflictProperty), conflictValue, new ServiceContext<NodeService>(service));
//		
//		if (isConflict(node)) {
//			// already set
//			return;
//		}
//		
//		// set conflict true
//		service.setProperty(node, CONFLICT, true, new ServiceContext<NodeService>(service));
//		
//		// propagate childrenConflict flag for parents
//		Node parent = null;
//		while ((parent = service.getParent(node, new ServiceContext<NodeService>(service))) != null) {
//			if (isChildrenConflict(parent)) {
//				// the childrenConflict flag has already been propagated
//				return;
//			}
//			// set childrenConflict false
//			service.setProperty(parent, CHILDREN_CONFLICT, true, new ServiceContext<NodeService>(service));
//			node = parent;
//		}
//	}

//	public static void setConflictFalseAndPropagateToParents(Node node, String conflictProperty, NodeService service) {		
//		
//		
//		// propagate childrenConflict flag for parents
//		Node parent = null;
//		while ((parent = service.getParent(node, new ServiceContext<NodeService>(service))) != null) {
//			if (!noChildConflict(parent, service)) {
//				return;
//			}
//			// if childrenConflict is already true for the parent, no need to go up
//			if (!isChildrenConflict(parent)) {
//				return;
//			}
//			// set childrenConflict false
//			service.setProperty(parent, CHILDREN_CONFLICT, false, new ServiceContext<NodeService>(service));
//			node = parent;
//		}
//	}
	
//	public static boolean allChildrenSync(Node node, NodeService service) {		
//		for (Node child : service.getChildren(node, new ServiceContext<NodeService>(service).add(POPULATE_WITH_PROPERTIES, false))) {
//			if (!isSync(child) || isAdded(child) || isRemoved(child)) {
//				return false;
//			}
//		}
//		return true;
//	}
	
//	public static boolean noChildConflict(Node node, NodeService service) {		
//		for (Node child : service.getChildren(node, new ServiceContext<NodeService>(service).add(POPULATE_WITH_PROPERTIES, true))) {
//			if (isConflict(child) || isChildrenConflict(child)) {
//				return false;
//			}
//		}
//		return true;
//	}
	
//	public static boolean isSync(Node node) {
//		return hasFlagTrue(node, SYNC);
//	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static boolean isChildrenSync(Node node) {
		return hasFlagTrue(node, CHILDREN_SYNC);
	}
	
//	public static boolean isAdded(Node node) {
//		return hasFlagTrue(node, ADDED);
//	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public static boolean isRemoved(Node node) {
		return hasFlagTrue(node, REMOVED);
	}
	
//	public static boolean isConflict(Node node) {
//		return hasFlagTrue(node, CONFLICT);
//	}
	
//	public static boolean isChildrenConflict(Node node) {
//		return hasFlagTrue(node, CHILDREN_CONFLICT);
//	}
	
	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getPropertyValue(flag);
		return b != null && b;
	}
}
