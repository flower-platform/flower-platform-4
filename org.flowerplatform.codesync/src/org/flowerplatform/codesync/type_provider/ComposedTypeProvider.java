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
package org.flowerplatform.codesync.type_provider;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.CodeSyncAlgorithm;

/**
 * @author Mariana Gheorghe
 */
public class ComposedTypeProvider implements ITypeProvider {

	private List<ITypeProvider> typeProviders = new ArrayList<ITypeProvider>();
	
	@Override
	public String getType(Object object, CodeSyncAlgorithm codeSyncAlgorithm) {
		for (ITypeProvider typeProvider : typeProviders) {
			String type = typeProvider.getType(object, codeSyncAlgorithm);
			if (type != null) {
				return type;
			}
		}
		throw new RuntimeException("Cannot provide type for " + object);
	}
	
	/**
	 * @author see class
	 */
	public ComposedTypeProvider addTypeProvider(ITypeProvider typeProvider) {
		typeProviders.add(typeProvider);
		return this;
	}

}