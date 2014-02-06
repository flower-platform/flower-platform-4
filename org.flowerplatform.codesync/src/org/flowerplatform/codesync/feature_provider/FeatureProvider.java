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

import java.util.List;

import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Mariana
 */
public abstract class FeatureProvider extends AbstractController {
	
	public static final String FEATURE_PROVIDER = "featureProvider";

	public abstract List<?> getFeatures(Object element);

	public abstract int getFeatureType(Object feature);
	
	public abstract String getFeatureName(Object feature);
	
}