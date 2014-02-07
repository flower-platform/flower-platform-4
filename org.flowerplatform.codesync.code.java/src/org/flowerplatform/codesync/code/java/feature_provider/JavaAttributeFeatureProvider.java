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
package org.flowerplatform.codesync.code.java.feature_provider;

import static org.flowerplatform.codesync.code.java.feature_provider.JavaFeaturesConstants.*;

import java.util.Arrays;
import java.util.List;

import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

public class JavaAttributeFeatureProvider extends NodeFeatureProvider {
	
	public static final String ATTRIBUTE_INITIALIZER = "attributeInitializer";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getValueFeatures(Object element) {
		List features = super.getValueFeatures(element);
		features.addAll(Arrays.asList(
				DOCUMENTATION,
				TYPED_ELEMENT_TYPE,
				ATTRIBUTE_INITIALIZER));
		return features;
	}

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return Arrays.asList(MODIFIERS);
	}
	
	@Override
	public int getFeatureType(Object feature) {
		if (MODIFIERS.equals(feature)) {
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		}
		return super.getFeatureType(feature);
	}
	
}