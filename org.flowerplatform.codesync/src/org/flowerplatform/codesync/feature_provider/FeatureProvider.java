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
package org.flowerplatform.codesync.feature_provider;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana
 */
public abstract class FeatureProvider extends AbstractController {
	
	protected List<Object> valueFeatures = new ArrayList<>();
	
	protected List<Object> containmentFeatures = new ArrayList<>();
	
	public List<?> getValueFeatures() {
		return valueFeatures;
	}
	
	public List<?> getContainmentFeatures() {
		return containmentFeatures;
	}
	
	public int getFeatureType(Object feature) {
		if (valueFeatures.contains(feature)) {
			return CodeSyncConstants.FEATURE_TYPE_VALUE;
		} else if (containmentFeatures.contains(feature)) {
			return CodeSyncConstants.FEATURE_TYPE_CONTAINMENT;
		}
		throw new RuntimeException("Feature " + getFeatureName(feature) + " is not registered");
	}
	
	public String getFeatureName(Object feature) {
		if (feature != null) {
			return feature.toString();
		}
		throw new RuntimeException("Feature is null");
	}

	@Override
	public String toString() {
		return String.format("FeatureProvider [containment = %s, value = %s, orderIndex = %d]", 
				containmentFeatures, valueFeatures, getOrderIndex());
	}
	
}