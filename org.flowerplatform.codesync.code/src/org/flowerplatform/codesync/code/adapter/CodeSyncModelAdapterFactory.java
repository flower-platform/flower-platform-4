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
package org.flowerplatform.codesync.code.adapter;

import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.ModelAdapterFactory;
import org.flowerplatform.codesync.adapter.ModelAdapterFactorySet;
import org.flowerplatform.codesync.adapter.NodeModelAdapter;
import org.flowerplatform.core.mindmap.remote.Node;

/**
 * @author Mariana
 */
public class CodeSyncModelAdapterFactory extends ModelAdapterFactory {

	private ModelAdapterFactorySet factorySet;
	private ModelAdapterFactory astModelAdapterFactory;
	private boolean isLeft;
	
	public CodeSyncModelAdapterFactory(ModelAdapterFactorySet factorySet, ModelAdapterFactory astModelAdapterFactory, /*Resource resource,*/ boolean isLeft) {
		this.factorySet = factorySet;
		this.astModelAdapterFactory = astModelAdapterFactory;
		this.isLeft = isLeft;
	}
	
	private IModelAdapter createModelAdapter(IModelAdapter adapter) {
		return adapter
//				.setModelAdapterFactory(this)
//				.setEObjectConverter(astModelAdapterFactory)
				.setModelAdapterFactorySet(factorySet);
	}
	
	@Override
	public IModelAdapter getModelAdapter(Object modelElement) {
		if (modelElement instanceof Node) {
			for (ModelAdapterEntry entry : modelAdapters) {
				String type = ((Node) modelElement).getType();
				if (type != null && type.equals(entry.type)) {
					return entry.modelAdapter;
				}
			}
		}
		return super.getModelAdapter(modelElement);
	}

	public ModelAdapterEntry addModelAdapter(String type, IModelAdapter modelAdapter, String adapterType) {
		ModelAdapterEntry e = new ModelAdapterEntry();
		e.type = type;
//		e.modelAdapter = isLeft 
//							? new NodeModelAdapterLeft() 
//							: new NodeModelAdapterAncestor();
		e.modelAdapter = modelAdapter;
		modelAdapter.setType(adapterType);
		modelAdapters.add(e);
		return e;
	}
	
	public ModelAdapterEntry addModelAdapter(Class<?> clazz, NodeModelAdapter modelAdapter, String adapterType) {
		modelAdapter.setType(adapterType);
		return super.addModelAdapter(clazz,
//				isLeft 
//					? new NodeModelAdapterLeft()
//					: new NodeModelAdapterAncestor(),
				modelAdapter,
				adapterType);
	}
	
}