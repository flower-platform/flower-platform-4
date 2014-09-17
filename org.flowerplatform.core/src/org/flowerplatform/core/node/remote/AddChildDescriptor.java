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

import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * Used to register client-side Add actions for a node type.
 * 
 * @author Mariana Gheorghe
 */
public class AddChildDescriptor extends AbstractController implements IDescriptor {

	private String childType;
	
	private String label;
	
	private String icon;

	public String getChildType() {
		return childType;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	//CHECKSTYLE:OFF
	public AddChildDescriptor setChildTypeAs(String childTypeValue) {
		this.childType = childTypeValue;
		return this;
	}

	public AddChildDescriptor setLabelAs(String label) {
		this.label = label;
		return this;
	}
	
	public AddChildDescriptor setIconAs(String icon) {
		this.icon = icon;
		return this;
	}

	public AddChildDescriptor setOrderIndexAs(int orderIndex) {
		setOrderIndex(orderIndex);
		return this;
	}
	//CHECKSTYLE:ON

	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setTypeDescriptor(TypeDescriptor typeDescriptor) {
		super.setTypeDescriptor(typeDescriptor);
		if (label == null) {
			label = ResourcesPlugin.getInstance().getLabelForNodeType(childType);
		}
	}

	@Override
	public String toString() {
		return String.format("AddChildDescriptor [childType = %s, label = %s, orderIndex = %d]", 
				childType, label, getOrderIndex());
	}
}