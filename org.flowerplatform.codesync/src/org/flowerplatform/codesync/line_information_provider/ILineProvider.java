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
package org.flowerplatform.codesync.line_information_provider;

import org.eclipse.jface.text.IDocument;
import org.flowerplatform.util.Pair;

/**
 * Provides the start line and end line for a model element.
 * The full text of the source file should also be provided,
 * in case it cannot be obtained from the model.
 * 
 * @author Mariana Gheorghe
 */
public interface ILineProvider {

	/**
	 *@author Mariana Gheorghe
	 **/
	Pair<Integer, Integer> getStartEndLines(Object model, IDocument document);
	
	/**
	 *@author Mariana Gheorghe
	 **/
	boolean canHandle(Object model);
}