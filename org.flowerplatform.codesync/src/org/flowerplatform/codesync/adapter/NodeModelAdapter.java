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
import static org.flowerplatform.codesync.CodeSyncConstants.REMOVED;
import static org.flowerplatform.core.CoreConstants.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;

import java.util.Iterator;
import java.util.List;

import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Mariana Gheorghe
 */
public class NodeModelAdapter extends AbstractModelAdapter {

	/**
	 * Checks for a {@link FeatureChange} on the name feature first.
	 */
	@Override
	public String getLabel(Object modelElement) {
		return (String) getMatchKey(modelElement);
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}
	
	/**
	 * Get the children that are registered as members of this feature (via {@link MemberOfChildCategoryDescriptor}s).
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, final Object feature, Iterable<?> correspondingIterable) {
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
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {		
		return getNode(element).getPropertyValue((String) feature);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getNode(element).getPropertyValue(CoreConstants.NAME);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {		
		CorePlugin.getInstance().getNodeService().setProperty(getNode(element), (String) feature, newValue, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild, ITypeProvider typeProvider) {
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
				Node parent = getNode(element);
				// set the type for the new node; needed by the action performed handler
				String type = typeProvider.getType(correspondingChild);
				
				String scheme = Utils.getScheme(parent.getNodeUri());
				String ssp = Utils.getSchemeSpecificPart(parent.getNodeUri());
				String childUri = Utils.getUri(scheme, ssp, null);
				Node child = new Node(childUri, type);
				CorePlugin.getInstance().getNodeService().addChild(parent, child, new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
				return child;
//		}
//		
//		return null;
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		CorePlugin.getInstance().getNodeService().removeChild(getNode(parent), getNode(child), new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
	}

	@Override
	public boolean hasChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean save(Object element) {
//		CodeSyncPlugin.getInstance().getNodeService().save();
		return false;
	}

	@Override
	public void setConflict(Object element, Object feature, Object oppositeValue) {		
		CodeSyncControllerUtils.setConflictTrueAndPropagateToParents(getNode(element), feature.toString(), oppositeValue, CorePlugin.getInstance().getNodeService());
	}
	
	@Override
	public void unsetConflict(Object element, Object feature) {		
		CodeSyncControllerUtils.setConflictFalseAndPropagateToParents(getNode(element), feature.toString(), CorePlugin.getInstance().getNodeService());
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
				Object childMatchKey = parentMatch.getCodeSyncAlgorithm().getLeftModelAdapter(match, child).getMatchKey(child);
				if (matchKey.equals(childMatchKey)) {
					return child;
				}
			}
		}
		
		return null;
	}
	
}
