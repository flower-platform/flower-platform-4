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

/**
 * Provides the start line and end line for a model element.
 * The full text of the source file should also be provided,
 * in case it cannot be obtained from the model.
 * 
 * @author Mariana Gheorghe
 */
public interface ILineInformationProvider {

	int getStartLine(Object model, IDocument document);
	int getEndLine(Object model, IDocument document);
	
	boolean canHandle(Object model);
}