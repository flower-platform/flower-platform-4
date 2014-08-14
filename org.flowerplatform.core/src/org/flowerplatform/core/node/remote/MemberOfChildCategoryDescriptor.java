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
package org.flowerplatform.core.node.remote;

import org.flowerplatform.util.controller.AbstractController;

/**
 * Used to filter the children of a node into categories (e.g. a Java class has three categories of children, 
 * body declarations, modifiers and super interfaces). This descriptor is a single controller, and is added
 * on the child type.
 * 
 * @author Mariana Gheorghe
 */
public class MemberOfChildCategoryDescriptor extends AbstractController {

	private String childCategory;

	/**
	 * @author see class
	 */
	public MemberOfChildCategoryDescriptor(String childCategory) {
		setChildCategory(childCategory);
	}
	
	public String getChildCategory() {
		return childCategory;
	}

	public void setChildCategory(String childCategory) {
		this.childCategory = childCategory;
	}
	
	@Override
	public String toString() {
		return String.format("MemberOfChildCategoryDescriptor [childCategory = %s, orderIndex = %d]", 
				getChildCategory(), getOrderIndex());
	}
	
}