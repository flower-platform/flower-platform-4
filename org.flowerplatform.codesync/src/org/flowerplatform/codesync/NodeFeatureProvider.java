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
package org.flowerplatform.codesync;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.core.mindmap.remote.Node;

/**
 * @author Mariana
 */
public class NodeFeatureProvider implements IFeatureProvider {

	public static String CHILDREN = "children";
	
	@Override
	public List<?> getFeatures(Object element) {
		List<String> features = new ArrayList<String>();
		features.add(Node.NAME);
		features.add(Node.TYPE);
		features.add(CHILDREN);
		return features;
	}

	@Override
	public int getFeatureType(Object feature) {
		if (feature.equals(CHILDREN)) {
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		} else {
			return IModelAdapter.FEATURE_TYPE_VALUE;
		}
	}

	@Override
	public String getFeatureName(Object feature) {
		return feature.toString();
	}

}