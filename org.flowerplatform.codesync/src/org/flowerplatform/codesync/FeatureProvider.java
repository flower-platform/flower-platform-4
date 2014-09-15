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
package org.flowerplatform.codesync;

import java.util.List;

import org.flowerplatform.codesync.adapter.IModelAdapter;

/**
 * @author Mariana Gheorghe
 */
public class FeatureProvider {

	private String extension;
	private IModelAdapter modelAdapter;
	private CodeSyncAlgorithm codeSyncAlgorithm;
	
	/**
	 *@author see class
	 **/
	public FeatureProvider(IModelAdapter modelAdapter, CodeSyncAlgorithm codeSyncAlgorithm) {
		this.modelAdapter = modelAdapter;
		this.codeSyncAlgorithm = codeSyncAlgorithm;
	}

	public List<?> getValueFeatures() {
		return modelAdapter.getValueFeatures(extension, codeSyncAlgorithm);
	}
	
	public List<?> getContainmentFeatures() {
		return modelAdapter.getContainmentFeatures(extension, codeSyncAlgorithm);
	}
	
	/**
	 *@author see class
	 **/
	public int getFeatureType(Object feature) {
		return modelAdapter.getFeatureType(extension, feature, codeSyncAlgorithm);
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}