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
package org.flowerplatform.util.controller;

/**
 * Base class for controllers. A controller, is a class that knows how to handle a specific operation
 * for a type (or several types) of objects (or nodes).
 * 
 * <p>
 * We can divide controllers into 2 categories:
 * <ul>
 * 	<li>an actual controller, that has one or several methods. It takes as parameter (at least) one
 * 		object (of type = the type for which the controller was registered in {@link TypeDescriptorRegistry}. E.g.
 * 		<code>getChildren(node)</code> provides logic to get children for a node of given type.
 * 	</li>
 * 	<li>a descriptor, that has no methods. It has some attributes that provide information that applies
 * 		to all nodes of a certain type (of type = the type for which the controller was registered in {@link TypeDescriptorRegistry}. E.g.
 * 		some <code>PropertyDescriptor</code> specifies what are the valid properties for a certain node.
 * 	</li> 
 * </ul>
 * 
 * Controllers are registered in the {@link TypeDescriptorRegistry}, for a node type(s) and/or node category(es). 
 * 
 * <p>
 * Controllers can be:
 * <ul>
 * 	<li>single. I.e. only one controller is allowed for a type.</li>
 * 	<li>additive. I.e. several controllers can be used for a type. Each one will be invoked when needed.</li>
 * </ul> 
 * 
 * @see TypeDescriptorRegistry
 * @see TypeDescriptor
 * 
 * @author Cristian Spiescu
 */
public abstract class AbstractController implements IController {
	
	/**
	 * @see #getOrderIndex()
	 */
	private int orderIndex;

	/**
	 * @author Claudiu Matei
	 */
	private boolean sharedControllerAllowed;
	
	/**
	 * @author Claudiu Matei
	 */
	private TypeDescriptor typeDescriptor; 
	
	/* (non-Javadoc)
	 * @see org.flowerplatform.util.controller.IOrderedController#getOrderIndex()
	 */
	@Override
	public int getOrderIndex() {
		return orderIndex;
	}
	
	/* (non-Javadoc)
	 * @see org.flowerplatform.util.controller.IOrderedController#setOrderIndex(int)
	 */
	@Override
	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}

	public IController setOrderIndexAs(int orderIndex) {
		setOrderIndex(orderIndex);
		return this;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setTypeDescriptor(TypeDescriptor typeDescriptor) {
		if (!sharedControllerAllowed && this.typeDescriptor !=null && this.typeDescriptor != typeDescriptor) {
			throw new IllegalStateException(String.format("This instance of %s cannot be registered for type '%s'. It is is already registered for type '%s'.", 
					this.getClass(), typeDescriptor.getType(), this.typeDescriptor.getType()));
		}
		this.typeDescriptor = typeDescriptor;
	}
	
	public boolean isSharedControllerAllowed() {
		return sharedControllerAllowed;
	}

	public void setSharedControllerAllowed(boolean sharedControllerAllowed) {
		this.sharedControllerAllowed = sharedControllerAllowed;
	}

	/**
	 * Needed to know how to sort the list of controllers. For additive controllers.
	 */
	@Override
	public int compareTo(IController o) {
		return Integer.compare(getOrderIndex(), o.getOrderIndex());
	}

	@Override
	public String toString() {
		return String.format("%s [orderIndex = %d]", getClass().getSimpleName(), getOrderIndex());
	}
	
}
