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
package org.flowerplatform.core.node.remote;

import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;

/**
 * A descriptor used to provide a value common to all nodes of a given type, as opposed to keeping
 * the value in the properties map of each node.
 * 
 * <p>
 * For example, the name of the property that gives the title or icons for all nodes of a given type
 * should <b>not</b> be put in the properties map of each node. Instead, a {@link GenericValueDescriptor}
 * with the name of the property should be registered, and used whenever the property is needed. 
 * If the property needs to be overwritten (e.g. a type would need to use a different property for its
 * title than the one registered by a category), then a new {@link GenericValueDescriptor} with a lower
 * order index may be registered for that type.
 * 
 * @author Mariana Gheorghe
 */
public class GenericValueDescriptor extends AbstractController implements IDescriptor {

	private Object value;
	
	/**
	 * @author see class
	 */
	public GenericValueDescriptor(Object value) {
		super();
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("GenericValueDescriptor [value = %s, orderIndex = %d]", 
				getValue(), getOrderIndex());
	}
	
}