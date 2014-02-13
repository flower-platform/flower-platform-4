/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.adapter;

import static org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor.MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.MemberOfChildCategoryDescriptor;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
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
		List<Node> children = CodeSyncPlugin.getInstance().getNodeService().getChildren(getNode(element), true);
		return new FilteredIterable<Node, Object>((Iterator<Node>) children.iterator()) {

			@Override
			protected boolean isAccepted(Node candidate) {
				TypeDescriptor candidateDescriptor = CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getExpectedTypeDescriptor(candidate.getType());
				if (candidateDescriptor == null) {
					return false;
				}
				
				MemberOfChildCategoryDescriptor memberOf = candidateDescriptor.getSingleController(MEMBER_OF_CHILD_CATEGORY_DESCRIPTOR);
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
		if (FeatureProvider.TYPE.equals(feature)) {
			return getNode(element).getType();
		}
		return getNode(element).getOrCreateProperties().get(feature);
	}
	
	@Override
	public Object getMatchKey(Object element) {
		return getNode(element).getOrCreateProperties().get("name");
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object newValue) {
		if (FeatureProvider.TYPE.equals(feature)) {
			getNode(element).setType((String) newValue);
		}
		CodeSyncPlugin.getInstance().getNodeService().setProperty(getNode(element), (String) feature, newValue);
	}
	
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
				Node child = new Node();
				child.setType(type);
				CodeSyncPlugin.getInstance().getNodeService().addChild(parent, child);
				return child;
//		}
//		
//		return null;
	}
	
	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		CodeSyncPlugin.getInstance().getNodeService().removeChild(getNode(parent), getNode(child));
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
		NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		CodeSyncControllerUtils.setConflictTrueAndPropagateToParents(getNode(element), feature.toString(), oppositeValue, service);
	}
	
	@Override
	public void unsetConflict(Object element, Object feature) {
		NodeService service = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		CodeSyncControllerUtils.setConflictFalseAndPropagateToParents(getNode(element), feature.toString(), service);
	}

	protected Node getNode(Object element) {
		return (Node) element;
	}
	
	protected void processContainmentFeatureAfterActionPerformed(Node node, Object feature, ActionResult result, Match match) {
		Object child = findChild(match, feature, result.childAdded, result.childMatchKey);
		if (child != null && child instanceof Node) {
			Node childNode = (Node) child;
			if (result.childAdded) {
				if (childNode.getOrCreateProperties().containsKey(CodeSyncPlugin.ADDED)) {
					CodeSyncPlugin.getInstance().getNodeService().unsetProperty(childNode, CodeSyncPlugin.ADDED);
				}
			} else {
				if (childNode.getOrCreateProperties().containsKey(CodeSyncPlugin.REMOVED)) {
					CodeSyncPlugin.getInstance().getNodeService().removeChild(node, childNode);
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
		if (matchKey == null)
			return null;
		for (Object existingChild : getChildrenFromMatch(parentMatch, feature, childAdded)) {
			if (existingChild == null) {
				continue;
			}
			Object childMatchKey = parentMatch.getCodeSyncAlgorithm().getLeftModelAdapter(existingChild).getMatchKey(existingChild);
			if (matchKey.equals(childMatchKey)) {
				return existingChild;
			}
		}
		return null;
	}

	protected List<Object> getChildrenFromMatch(Match parentMatch, Object feature, boolean childAdded) {
		List<Object> children = new ArrayList<Object>();
		for (Match match : parentMatch.getSubMatches()) {
			if (match.getFeature().equals(feature)) {
				if (childAdded) {
					children.add(match.getLeft());
				} else {
					children.add(match.getAncestor());
				}
			}
		}
		return children;
	}
	
}