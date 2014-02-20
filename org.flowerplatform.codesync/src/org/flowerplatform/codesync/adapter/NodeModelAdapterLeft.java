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

import java.util.Iterator;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.codesync.controller.CodeSyncControllerUtils;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;

/**
 * @author Mariana
 */
public class NodeModelAdapterLeft extends NodeModelAdapter {

	/**
	 * Filters out deleted {@link Node}s from the containment list for <code>feature</code>.
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(final Object element, Object feature, Iterable<?> correspondingIterable) {
		Iterable<?> children = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
		// filter out deleted elements
		return new FilteredIterable<Object, Object>((Iterator<Object>) children.iterator()) {
			protected boolean isAccepted(Object candidate) {
				Boolean isRemoved = (Boolean) getNode(candidate).getOrPopulateProperties().get(CodeSyncPlugin.REMOVED_MARKER);
				if (isRemoved != null && isRemoved) {
					return false;
				}
				return true;
			}
		
		};
	}
	
	/**
	 * Before the features are processed for <code>element</code>, checks if the AST cache was deleted, and 
	 * recreates it. Note: we cannot do this while the features are processed because upon requesting the 
	 * value for a 2nd feature, the AST cache will be refreshed again, thus losing the value for a previously
	 * processed feature.
	 */
	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
//		CodeSyncElement cse = getCodeSyncElement(element);
//		if (cse != null) {
//			if (cse.getAstCacheElement() == null || cse.getAstCacheElement().eResource() == null) {
//				AstCacheElement ace = (AstCacheElement) createCorrespondingModelElement(correspondingElement);
//				cse.setAstCacheElement(ace);
//			}
//		}
	}

	/**
	 * Adds the {@link AstCacheElement} to the AST cache resource.
	 */
	@Override
	public void featuresProcessed(Object element) {
//		CodeSyncElement cse = getCodeSyncElement(element);
//		if (cse != null) {
//			AstCacheElement ace = cse.getAstCacheElement();
//			if (ace != null && ace.eResource() == null) {
//				addToResource(ace);
//			}
//		}
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement, CodeSyncAlgorithm codeSyncAlgorithm) {
		Node node = getNode(element);
		NodeService nodeService = (NodeService) CorePlugin.getInstance().getServiceRegistry().getService("nodeService");
		CodeSyncControllerUtils.setSyncTrueAndPropagateToParents(node, nodeService);
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result, Match match) {
		if (result == null || result.conflict) {
			return;
		}

		Node node = getNode(element);
		int featureType = match.getCodeSyncAlgorithm().getFeatureProvider(node).getFeatureType(feature);
		switch (featureType) {
		case IModelAdapter.FEATURE_TYPE_VALUE:
			CodeSyncPlugin.getInstance().getNodeService().unsetProperty(node, CodeSyncControllerUtils.getOriginalPropertyName(feature.toString()));
			break;
		case IModelAdapter.FEATURE_TYPE_CONTAINMENT:
			processContainmentFeatureAfterActionPerformed(node, feature, result, match);
			break;
		default:
			break;
		}
	}
	
}