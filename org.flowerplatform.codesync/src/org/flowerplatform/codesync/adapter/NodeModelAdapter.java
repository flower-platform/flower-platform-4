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
package org.flowerplatform.codesync.adapter;

import static org.flowerplatform.codesync.CodeSyncConstants.ADDED;
import static org.flowerplatform.codesync.CodeSyncConstants.CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.CHILDREN_SYNC;
import static org.flowerplatform.codesync.CodeSyncConstants.CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.CONFLICT_SUFFIX;
import static org.flowerplatform.codesync.CodeSyncConstants.ORIGINAL_SUFFIX;
import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
import static org.flowerplatform.codesync.CodeSyncConstants.SYNC;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.Iterator;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Mariana Gheorghe
 */
public class NodeModelAdapter extends AbstractModelAdapter {

	/**
	 * Get the children that are registered as members of this feature (via {@link MemberOfChildCategoryDescriptor}s).
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, final Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		List<Node> children = CorePlugin.getInstance().getNodeService().getChildren(getNode(element), new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()).add(POPULATE_WITH_PROPERTIES, true));
		return new FilteredIterable<Node, Object>((Iterator<Node>) children.iterator()) {

			@Override
			protected boolean isAccepted(Node candidate) {
				TypeDescriptor candidateDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(candidate.getType());
				if (candidateDescriptor == null) {
					return false;
				}
				
				MemberOfChildCategoryDescriptor memberOf = candidateDescriptor.getSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR, candidate);
				if (memberOf == null) {
					return false;
				}
				if (feature.equals(memberOf.getChildCategory())) {
					return true;
				}
				return false;
			}
			
		};
	}
	
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {		
		return getNode(element).getPropertyValue((String) feature);
	}
	
	@Override
	public Object getMatchKey(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getNode(element).getPropertyValue(CoreConstants.NAME);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		context.getContext().put(CodeSyncConstants.SYNC_IN_PROGRESS, true);		
		CorePlugin.getInstance().getNodeService().setProperty(getNode(element), (String) feature, newValue, context);
	}
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild, IModelAdapterSet correspondingModelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		// first check if the child already exists
//		Iterable<?> children = super.getContainmentFeatureIterable(eObject, feature, null);
//		IModelAdapter adapter = codeSyncElementConverter.getModelAdapter(correspondingChild);
//		Object matchKey = null;
//		if (adapter != null) {
//			matchKey = adapter.getMatchKey(correspondingChild);
//		} else {
//			matchKey = getMatchKey(correspondingChild);
//		}
//		if (matchKey != null) {
//			for (Object child : children) {
//				if (matchKey.equals(modelAdapterFactory.getModelAdapter(child).getMatchKey(child))) {
//					return child;
//				}
//			}
//		}
//		
//		if (eObject != null) {
				Node parentNode = getNode(parent);
				// set the type for the new node; needed by the action performed handler
				String type = correspondingModelAdapterSet.getType(correspondingChild, codeSyncAlgorithm);
				
				String scheme = Utils.getScheme(parentNode.getNodeUri());
				String ssp = Utils.getSchemeSpecificPart(parentNode.getNodeUri());
				String childUri = Utils.getUri(scheme, ssp, null);
				Node child = new Node(childUri, type);
				ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
				context.getContext().put(CodeSyncConstants.SYNC_IN_PROGRESS, true);
				CorePlugin.getInstance().getNodeService().addChild(parentNode, child, context);
				return child;
//		}
//		
//		return null;
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child, CodeSyncAlgorithm codeSyncAlgorithm) {
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		context.getContext().put(CodeSyncConstants.SYNC_IN_PROGRESS, true);
		CorePlugin.getInstance().getNodeService().removeChild(getNode(parent), getNode(child), context);
	}

	@Override
	public boolean save(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(getNode(element).getNodeUri());
		String resourceSet = (String) resourceNode.getProperties().get(CoreConstants.RESOURCE_SET);
		if (resourceSet == null) {
			resourceSet = resourceNode.getNodeUri();
		}
		CorePlugin.getInstance().getResourceSetService().save(resourceSet, 
				new ServiceContext<ResourceSetService>(CorePlugin.getInstance().getResourceSetService()));
		return false;
	}

	public static String getConflictPropertyName(String property) {
		return property + CONFLICT_SUFFIX;
	}

	public static boolean isConflict(Node node) {
		return hasFlagTrue(node, CONFLICT);
	}

	public static boolean isConflictPropertyName(String property) {
		return property.endsWith(CONFLICT_SUFFIX);
	}

	public static boolean isOriginalPropertyName(String property) {
		return property.endsWith(ORIGINAL_SUFFIX);
	}

	private static boolean hasFlagTrue(Node node, String flag) {
		Boolean b = (Boolean) node.getPropertyValue(flag);
		return b != null && b;
	}

	public static boolean noChildConflict(Node node, NodeService service) {
		for (Node child : service.getChildren(node, new ServiceContext<NodeService>(service).add(POPULATE_WITH_PROPERTIES, true))) {
			if (isConflict(child) || isChildrenConflict(child)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isChildrenConflict(Node node) {
		return hasFlagTrue(node, CHILDREN_CONFLICT);
	}

	public static boolean isSync(Node node) {
		return hasFlagTrue(node, SYNC);
	}

	public static boolean isAdded(Node node) {
		return hasFlagTrue(node, ADDED);
	}

	public static boolean isRemoved(Node node) {
		return hasFlagTrue(node, REMOVED);
	}
	
	@Override
	public void setConflict(Object element, Object feature, Object conflictValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		Node node = getNode(element);
		String conflictProperty = feature.toString();
		NodeService service = CorePlugin.getInstance().getNodeService();

		service.setProperty(node, getConflictPropertyName(conflictProperty), conflictValue, new ServiceContext<NodeService>(service));

		if (isConflict(node)) {
			// already set
			return;
		}

		// set conflict true
		service.setProperty(node, CONFLICT, true, new ServiceContext<NodeService>(service));
	}
	
	@Override
	public void unsetConflict(Object element, Object feature, CodeSyncAlgorithm codeSyncAlgorithm) {
		Node node = getNode(element);
		String conflictProperty = feature.toString();
		NodeService service = CorePlugin.getInstance().getNodeService();

		if (!isConflict(node)) {
			return;
		}

		service.unsetProperty(node, getConflictPropertyName(conflictProperty), new ServiceContext<NodeService>(service));

		// check if it has any other property.conflict set
		boolean conflict = false;
		for (String property : node.getProperties().keySet()) {
			if (isConflictPropertyName(property)) {
				conflict = true;
			}
		}
		if (conflict) {
			return;
		}

		// set conflict false
		service.setProperty(node, CONFLICT, false, new ServiceContext<NodeService>(service));
	}
	
	@Override
	public void setChildrenConflict(Object element) {
		Node node = getNode(element);
		NodeService service = CorePlugin.getInstance().getNodeService();
		service.setProperty(node, CHILDREN_CONFLICT, true, new ServiceContext<NodeService>(service));
	}

	@Override
	public void unsetChildrenConflict(Object element) {
		Node node = getNode(element);
		NodeService service = CorePlugin.getInstance().getNodeService();
		service.setProperty(node, CHILDREN_CONFLICT, false, new ServiceContext<NodeService>(service));
	}
	
	@Override
	public void setSync(Object element) {
		Node node = getNode(element);
		NodeService service = CorePlugin.getInstance().getNodeService();

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
		for (String property : node.getProperties().keySet()) {
			if (isOriginalPropertyName(property)) {
				sync = false;
			}
		}
		if (!sync) {
			return;
		}

		// set sync true
		service.setProperty(node, SYNC, true, new ServiceContext<NodeService>(service));
	}
	
	@Override
	public void setChildrenSync(Object element) {
		Node node = getNode(element);
		NodeService service = CorePlugin.getInstance().getNodeService();
		service.setProperty(node, CHILDREN_SYNC, true, new ServiceContext<NodeService>(service));
	}

	protected Node getNode(Object element) {
		return (Node) element;
	}
	
	protected void processContainmentFeatureAfterActionPerformed(Node node, Object feature, ActionResult result, Match match) {
		NodeService service = CorePlugin.getInstance().getNodeService();
		Object child = findChild(match, feature, result.childAdded, result.childMatchKey);
		if (child != null && child instanceof Node) {
			Node childNode = (Node) child;
			if (result.childAdded) {
				if (childNode.getProperties().containsKey(ADDED)) {
					service.unsetProperty(childNode, ADDED, new ServiceContext<NodeService>(service));
				}
			} else {
				if (childNode.getProperties().containsKey(REMOVED)) {
					service.removeChild(node, childNode, new ServiceContext<NodeService>(service));
					// set childrenSync now, because after this match is synced, the parent won't be notified because this child is already removed
					CodeSyncControllerUtils.setChildrenSyncTrueAndPropagateToParents(node, service);
				}
			}
		}
	}
	
	/**
	 * Checks if the <code>list</code> contains the <code>child</code> based on its match key.
	 * @param matchKey 
	 * @param childMatchKey 
	 */
	protected Object findChild(Match parentMatch, Object feature, boolean childAdded, Object matchKey) {
		if (matchKey == null) {
			return null;
		}

		for (Match match : parentMatch.getSubMatches()) {
			Object child = null;
			if (match.getFeature().equals(feature)) {
				if (childAdded) {
					child = match.getLeft();
				} else {
					child = match.getAncestor();
				}
			}
			if (child != null) {
				Object childMatchKey = parentMatch.getCodeSyncAlgorithm().getLeftModelAdapter(child).getMatchKey(child, match.getCodeSyncAlgorithm());
				if (matchKey.equals(childMatchKey)) {
					return child;
				}
			}
		}
		
		return null;
	}
	
}
