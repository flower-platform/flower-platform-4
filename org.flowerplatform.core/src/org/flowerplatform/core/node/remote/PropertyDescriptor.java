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

import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY;
import static org.flowerplatform.util.UtilConstants.PROPERTY_EDITOR_TYPE_STRING;
import static org.flowerplatform.core.CoreConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT;

import java.util.List;

import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;
import org.flowerplatform.util.controller.TypeDescriptor;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class PropertyDescriptor extends AbstractController implements IDescriptor {
	
	private String name;
	private String label;
	
	private String type = PROPERTY_EDITOR_TYPE_STRING;
	private String group = PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY;
	
	private String propertyLineRenderer = PROPERTY_LINE_RENDERER_TYPE_DEFAULT;
	
	private boolean writeableOnCreate;
	
	private boolean readOnly;
	
	private List<?> possibleValues;
	
	private Object defaultValue;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	//CHECKSTYLE:OFF
	public PropertyDescriptor setNameAs(String name) {
		this.name = name;
		return this;
	}
	//CHECKSTYLE:ON
		
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	//CHECKSTYLE:OFF
	public PropertyDescriptor setLabelAs(String label) {
		this.label = label;
		return this;
	}
	//CHECKSTYLE:ON
	
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	//CHECKSTYLE:OFF

	public PropertyDescriptor setTypeAs(String type) {
		this.type = type;
		return this;
	}
	//CHECKSTYLE:ON

	public String getGroup() {
		return group;
	}
	
	public void setGroup(String category) {
		this.group = category;
	}
	
	//CHECKSTYLE:OFF

	public PropertyDescriptor setGroupAs(String group) {
		this.group = group;
		return this;
	}
	//CHECKSTYLE:ON

	
	public boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 *@author see class
	 **/
	public PropertyDescriptor setReadOnlyAs(boolean readOnlyValue) {
		this.readOnly = readOnlyValue;
		return this;
	}

	public List<?> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<Object> possibleValues) {
		this.possibleValues = possibleValues;
	}
	
	/**
	 *@author Mariana Gheorghe
	 **/
	public PropertyDescriptor setPossibleValuesAs(List<?> givenPossibleValues) {
		this.possibleValues = givenPossibleValues;
		return this;
	}
	
	public boolean getWriteableOnCreate() {
		return writeableOnCreate;
	}

	public void setWriteableOnCreate(boolean contributeToCreation) {
		this.writeableOnCreate = contributeToCreation;
	}
	
	/**
	 *@author see class
	 **/
	public PropertyDescriptor setContributesToCreationAs(boolean contributeToCreation) {
		this.writeableOnCreate = contributeToCreation;
		return this;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	//CHECKSTYLE:OFF
	public PropertyDescriptor setDefaultValueAs(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
		//CHECKSTYLE:ON
	}
	
	public String getPropertyLineRenderer() {
		return propertyLineRenderer;
	}

	public void setPropertyLineRenderer(String propertyLineRenderer) {
		this.propertyLineRenderer = propertyLineRenderer;
	}
	
	/**
	 *@author see class
	 **/
	public PropertyDescriptor setPropertyLineRendererAs(String lineRenderer) {
		this.propertyLineRenderer = lineRenderer;
		return this;
	}
	
	/**
	 * @author Claudiu Matei
	 */
	@Override
	public void setTypeDescriptor(TypeDescriptor typeDescriptor) {
		super.setTypeDescriptor(typeDescriptor);
		if (label == null) {
			label = ResourcesPlugin.getInstance().getLabelForProperty(typeDescriptor.getType() + "." + name);
		}
	}

	@Override
	public String toString() {
		return "PropertyDescriptor [name=" + name + ", title=" + label
				+ ", type=" + type + ", category=" + group
				+ ", propertyLineRenderer=" + propertyLineRenderer
				+ ", writeableOnCreate=" + writeableOnCreate
				+ ", readOnly=" + readOnly
				+ ", possibleValues=" + possibleValues + ", defaultValue="
				+ defaultValue + "]";
	}	

}
