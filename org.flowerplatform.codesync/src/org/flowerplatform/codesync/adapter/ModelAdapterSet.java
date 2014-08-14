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
package org.flowerplatform.codesync.adapter;

import static org.flowerplatform.codesync.CodeSyncConstants.CATEGORY_CODESYNC;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.line_information_provider.ILineProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.util.Pair;

/**
 * @author Mariana Gheorghe
 */
public class ModelAdapterSet implements IModelAdapterSet {

	private ITypeProvider typeProvider;
	
	private Map<String, IModelAdapter> modelAdapters = new HashMap<String, IModelAdapter>();
	
	private ILineProvider lineProvider;
	
	private IModelAdapter fileModelAdapterDelegate;

	@Override
	public String getType(Object model, CodeSyncAlgorithm codeSyncAlgorithm) { 
		return typeProvider.getType(model, codeSyncAlgorithm);
	}
	
	@Override
	public IModelAdapter getModelAdapter(Object model, CodeSyncAlgorithm codeSyncAlgorithm) {
		return getModelAdapterForType(getType(model, codeSyncAlgorithm));
	}
	
	@Override
	public IModelAdapter getModelAdapterForType(String type) {
		IModelAdapter adapter = modelAdapters.get(type);
		if (adapter != null) {
			return adapter;
		}
		return modelAdapters.get(CATEGORY_CODESYNC);
	}
	
	@Override
	public IModelAdapter getFileModelAdapterDelegate() {
		return fileModelAdapterDelegate;
	}
	
	@Override
	public Pair<Integer, Integer> getStartEndLine(Object model, IDocument document) {
		if (lineProvider.canHandle(model)) {
			return lineProvider.getStartEndLines(model, document);
		}
		return null;
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public ModelAdapterSet addModelAdapter(String type, IModelAdapter adapter) {
		modelAdapters.put(type, adapter);
		return this;
	}
	
	/**
	 *@author Mariana Gheorghe
	 */
	public ModelAdapterSet setTypeProvider(ITypeProvider typeProvider) {
		this.typeProvider = typeProvider;
		return this;
	}

	/**
	 *@author Mariana Gheorghe 
	 */
	public ModelAdapterSet setLineProvider(ILineProvider lineProvider) {
		this.lineProvider = lineProvider;
		return this;
	}

	/**
	 *@author Mariana Gheorghe
	 */
	public ModelAdapterSet setFileModelAdapterDelegate(IModelAdapter fileModelAdapterDelegate) {
		this.fileModelAdapterDelegate = fileModelAdapterDelegate;
		return this;
	}

}