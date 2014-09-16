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
package org.flowerplatform.util.controller;

/**
 * Same as its superclass, but disables {@link #addCategory(String)}, so that categories 
 * cannot belong to categories.
 * 
 * <p>
 * We do this, because it would increase the complexity of the code of these classes (e.g. check for
 * infinite cycles), and it would make room for structures that are not very easy to work with.
 * 
 * @author Cristian Spiescu
 */
public class CategoryTypeDescriptor extends TypeDescriptor {
	/**
	 *@author see class
	 **/
	public CategoryTypeDescriptor(TypeDescriptorRegistry registry, String type) {
		super(registry, type);
	}

	@Override
	public TypeDescriptor addCategory(String category) {
		throw new UnsupportedOperationException("Categories cannot belong to other categories");
	}

}