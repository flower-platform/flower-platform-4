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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;

/**
 * @author Mariana Gheorghe
 */
public class ComposedLineInformationProvider implements ILineInformationProvider {

	private List<ILineInformationProvider> lineInformationProviders = new ArrayList<ILineInformationProvider>();
	
	public void addLineInformationProvider(ILineInformationProvider provider) {
		lineInformationProviders.add(provider);
	}
	
	@Override
	public int getStartLine(Object model, IDocument document) {
		for (ILineInformationProvider lineInformationProvider : lineInformationProviders) {
			if (lineInformationProvider.canHandle(model)) {
				return lineInformationProvider.getStartLine(model, document);
			}
		}
		return -1;
	}

	@Override
	public int getEndLine(Object model, IDocument document) {
		for (ILineInformationProvider lineInformationProvider : lineInformationProviders) {
			if (lineInformationProvider.canHandle(model)) {
				return lineInformationProvider.getEndLine(model, document);
			}
		}
		return -1;
	}

	@Override
	public boolean canHandle(Object model) {
		return true;
	}

}