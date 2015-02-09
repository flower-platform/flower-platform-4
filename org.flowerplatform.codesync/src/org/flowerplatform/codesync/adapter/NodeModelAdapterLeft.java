/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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


import java.util.Iterator;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.FilteredIterable;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.action.ActionResult;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana
 */
public class NodeModelAdapterLeft extends NodeModelAdapter {

	/**
	 * Filters out deleted {@link Node}s from the containment list for <code>feature</code>.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<?> getContainmentFeatureIterable(final Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		Iterable<?> children = super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
		// filter out deleted elements
		return new FilteredIterable<Object, Object>((Iterator<Object>) children.iterator()) {
			protected boolean isAccepted(Object candidate) {
				Boolean isRemoved = (Boolean) getNode(candidate).getPropertyValue(CodeSyncConstants.REMOVED);
				if (isRemoved != null && isRemoved) {
					return false;
				}
				return true;
			}
		
		};
	}
	
	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result, Match match) {
		if (result == null || result.conflict) {
			return;
		}

		Node node = getNode(element);
		int featureType = match.getCodeSyncAlgorithm().getFeatureProvider(match).getFeatureType(feature);
		switch (featureType) {
		case CodeSyncConstants.FEATURE_TYPE_VALUE:
			CorePlugin.getInstance().getNodeService().unsetProperty(node, feature.toString() + CodeSyncConstants.ORIGINAL_SUFFIX,
					new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
			break;
		case CodeSyncConstants.FEATURE_TYPE_CONTAINMENT:
			processContainmentFeatureAfterActionPerformed(node, feature, result, match);
			break;
		default:
			break;
		}
	}
	
}
