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

import java.util.Collections;
import java.util.List;

import org.flowerplatform.codesync.feature_provider.NodeFeatureProvider;

public class JavaMemberValuePairFeatureProvider extends NodeFeatureProvider {
	
	public static final String ANNOTATION_VALUE_VALUE = "annotationValueValue";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getValueFeatures(Object element) {
		List features = super.getValueFeatures(element);
		features.add(ANNOTATION_VALUE_VALUE);
		return features;
	}

	@Override
	public List<?> getContainmentFeatures(Object element) {
		return Collections.emptyList();
	}
}