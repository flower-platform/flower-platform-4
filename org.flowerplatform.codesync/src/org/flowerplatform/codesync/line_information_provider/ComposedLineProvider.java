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
package org.flowerplatform.codesync.line_information_provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.flowerplatform.util.Pair;

/**
 * @author Mariana Gheorghe
 */
public class ComposedLineProvider implements ILineProvider {

	private List<ILineProvider> lineProviders = new ArrayList<ILineProvider>();
	
	/**
	 *@author see class
	 **/
	public void addLineProvider(ILineProvider provider) {
		lineProviders.add(provider);
	}
	
	@Override
	public Pair<Integer, Integer> getStartEndLines(Object model, IDocument document) {
		for (ILineProvider lineInformationProvider : lineProviders) {
			if (lineInformationProvider.canHandle(model)) {
				return lineInformationProvider.getStartEndLines(model, document);
			}
		}
		return null;
	}

	@Override
	public boolean canHandle(Object model) {
		return true;
	}

}