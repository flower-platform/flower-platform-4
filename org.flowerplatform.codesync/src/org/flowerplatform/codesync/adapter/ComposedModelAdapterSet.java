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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.util.Pair;

/**
 * 
 * @author Mariana Gheorghe
 */
public class ComposedModelAdapterSet implements IModelAdapterSet {

	private List<IModelAdapterSet> modelAdapterSets = new ArrayList<IModelAdapterSet>();
	
	/**
	 *@author see class
	 **/
	public ComposedModelAdapterSet addModelAdapterSet(IModelAdapterSet modelAdapterSet) {
		modelAdapterSets.add(modelAdapterSet);
		return this;
	}

	@Override
	public String getType(Object model, CodeSyncAlgorithm codeSyncAlgorithm) {
		for (IModelAdapterSet set : modelAdapterSets) {
			String type = set.getType(model, codeSyncAlgorithm);
			if (type != null) {
				return type;
			}
		}
		throw new RuntimeException("No type registered for " +  model);
	}

	@Override
	public IModelAdapter getModelAdapter(Object model, CodeSyncAlgorithm codeSyncAlgorithm) {
		for (IModelAdapterSet set : modelAdapterSets) {
			IModelAdapter adapter = set.getModelAdapter(model, codeSyncAlgorithm);
			if (adapter != null) {
				return adapter;
			}
		}
		throw new RuntimeException("No model adapter registered for " + model);
	}

	@Override
	public IModelAdapter getModelAdapterForType(String type) {
		for (IModelAdapterSet set : modelAdapterSets) {
			IModelAdapter adapter = set.getModelAdapterForType(type);
			if (adapter != null) {
				return adapter;
			}
		}
		throw new RuntimeException("No model adapter registered for type " + type);
	}

	@Override
	public IModelAdapter getFileModelAdapterDelegate() {
		throw new UnsupportedOperationException("Cannot get file model adapter delegate for composed set");
	}
	
	@Override
	public Pair<Integer, Integer> getStartEndLine(Object model, IDocument document) {
		for (IModelAdapterSet set : modelAdapterSets) {
			Pair<Integer, Integer> lines = set.getStartEndLine(model, document);
			if (lines != null) {
				return lines;
			}
		}
		return null;
	}
	
}