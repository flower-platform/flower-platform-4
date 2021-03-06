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

import org.eclipse.jface.text.IDocument;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.util.Pair;

/**
 * @author Mariana Gheorghe
 */
public interface IModelAdapterSet {

	
	/**
	 *@author Mariana Gheorghe
	 **/
	String getType(Object model, CodeSyncAlgorithm codeSyncAlgorithm);

	
	/**
	 *@author Mariana Gheorghe
	 **/
	IModelAdapter getModelAdapter(Object model, CodeSyncAlgorithm codeSyncAlgorithm);

	
	/**
	 *@author Mariana Gheorghe
	 **/
	IModelAdapter getModelAdapterForType(String type);

	
	/**
	 *@author Mariana Gheorghe
	 **/
	IModelAdapter getFileModelAdapterDelegate();
	
	
	/**
	 *@author Mariana Gheorghe
	 **/
	Pair<Integer, Integer> getStartEndLine(Object model, IDocument document);
	
}