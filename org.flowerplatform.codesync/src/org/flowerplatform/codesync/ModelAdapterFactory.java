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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModelAdapterFactory {
	
	protected static class ModelAdapterEntry {
		public Class<?> clazz;
		public String type;
		public IModelAdapter modelAdapter;
		public String extension;
		
		public ModelAdapterEntry() {
		}

		@Override
		public String toString() {
			return clazz.getSimpleName() + " " + modelAdapter.getClass().getSimpleName(); 
		}
	}
	
	/**
	 * O vom optimiza probabil cu un hashmap. Asta este modelul pentru care nu
	 * am pus functia de genul "IModelAdapter.isForType()", care ar fi implicat mereu
	 * o iteratie.
	 */
	protected List<ModelAdapterEntry> modelAdapters = new ArrayList<ModelAdapterEntry>();
	
	/**
	 * @author Cristi
	 * @author Mariana
	 */
	public IModelAdapter getModelAdapter(Object modelElement) {
		for (ModelAdapterEntry e : modelAdapters)
			if (e.clazz != null && e.clazz.isAssignableFrom(modelElement.getClass())) {
				if (!(modelElement instanceof File) || CodeSyncPlugin.getInstance().getFileExtension(((File) modelElement)).equals(e.extension)) {
					return e.modelAdapter;
				}
			}
		return null;
	}
	
	/**
	 * Finds an adapter based on a type, not on the element
	 * it adapts; used to retrieve the adapter that will 
	 * create the left-side cache when the element on the
	 * right-side does not exist.
	 * 
	 * @param type set on the {@link IModelAdapter}
	 * 
	 * @author Mariana Gheorghe
	 */
	public IModelAdapter getModelAdapterByType(String type) {
		for (ModelAdapterEntry entry : modelAdapters) {
			if (entry.modelAdapter.getType().equals(type)) {
				return entry.modelAdapter;
			}
		}
		return null;
	}

	public ModelAdapterEntry addModelAdapter(Class<?> clazz, IModelAdapter modelAdapter, String adapterType) {
		ModelAdapterEntry e = new ModelAdapterEntry();
		e.clazz = clazz;
		e.modelAdapter = modelAdapter;
		modelAdapter.setType(adapterType);
		modelAdapters.add(e);
		return e;
	}
	
	/**
	 * @author Mariana
	 */
	public ModelAdapterEntry addModelAdapter(Class<?> clazz, IModelAdapter modelAdapter, String extension, String adapterType) {
		ModelAdapterEntry e = addModelAdapter(clazz, modelAdapter, adapterType);
		e.extension = extension;
		return e;
	}
}