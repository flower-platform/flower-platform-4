/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.core.node.remote;

import java.util.Map;

import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;

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
	
	public AddChildDescriptor setChildTypeAs(String childType) {
		this.childType = childType;
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
	
	@Override
	public String toString() {
		return String.format("\nAddChildDescriptor [childType = %s, label = %s, orderIndex = %d]", 
				childType, label, getOrderIndex());
	}
}
