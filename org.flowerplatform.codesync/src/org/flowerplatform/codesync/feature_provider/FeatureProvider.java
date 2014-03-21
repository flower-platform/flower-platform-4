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
package org.flowerplatform.codesync.feature_provider;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana
 */
public abstract class FeatureProvider extends AbstractController {
	
	public static final String FEATURE_PROVIDER = "featureProvider";

	protected List<Object> valueFeatures = new ArrayList<>();
	
	protected List<Object> containmentFeatures = new ArrayList<>();
	
	public List<?> getValueFeatures(Object element) {
		return valueFeatures;
	}
	
	public List<?> getContainmentFeatures(Object element) {
		return containmentFeatures;
	}
	
	public int getFeatureType(Object feature) {
		if (valueFeatures.contains(feature)) {
			return IModelAdapter.FEATURE_TYPE_VALUE;
		} else if (containmentFeatures.contains(feature)) {
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		}
		throw new RuntimeException("Feature " + getFeatureName(feature) + " is not registered");
	}
	
	public String getFeatureName(Object feature) {
		if (feature != null) {
			return feature.toString();
		}
		throw new RuntimeException("Feature is null");
	}
	
}