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
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;
import static org.flowerplatform.core.CoreConstants.PROPERTY_LINE_RENDERER_TYPE_DEFAULT;

import java.util.List;

import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;

/**
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class PropertyDescriptor extends AbstractController implements IDescriptor {
	
	private String name;
	private String title;
	
	private String type = PROPERTY_DESCRIPTOR_TYPE_STRING;
	private String category = PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY;
	
	private String propertyLineRenderer = PROPERTY_LINE_RENDERER_TYPE_DEFAULT;
	
	private boolean contributesToCreation;
	private boolean mandatory;
	
	private boolean readOnly;
	
	private List<?> possibleValues;
	
	private Object defaultValue;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setNameAs(String name) {
		this.name = name;
		return this;
	}
	//CHECKSTYLE:ON
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setTitleAs(String title) {
		this.title = title;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setTypeAs(String type) {
		this.type = type;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setCategoryAs(String category) {
		this.category = category;
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
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setReadOnlyAs(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}
	//CHECKSTYLE:ON

	public List<?> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<Object> possibleValues) {
		this.possibleValues = possibleValues;
	}
	
	/**
	 * @author Mariana Gheorghe
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setPossibleValuesAs(List<?> possibleValues) {
		this.possibleValues = possibleValues;
		return this;
	}
	//CHECKSTYLE:ON
	
	public boolean getContributesToCreation() {
		return contributesToCreation;
	}

	public void setContributesToCreation(boolean contributeToCreation) {
		this.contributesToCreation = contributeToCreation;
	}
	
	/**
	 * @author see class
	 */
	public PropertyDescriptor setContributesToCreationAs(boolean contributeToCreation) {
		this.contributesToCreation = contributeToCreation;
		return this;
	}

	public boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setMandatoryAs(boolean mandatory) {
		this.mandatory = mandatory;
		return this;
	}
	//CHECKSTYLE:ON
	
	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setDefaultValueAs(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getPropertyLineRenderer() {
		return propertyLineRenderer;
	}

	public void setPropertyLineRenderer(String propertyLineRenderer) {
		this.propertyLineRenderer = propertyLineRenderer;
	}
	
	/**
	 * @author see class
	 */
	//CHECKSTYLE:OFF
	public PropertyDescriptor setPropertyLineRendererAs(String propertyLineRenderer) {
		this.propertyLineRenderer = propertyLineRenderer;
		return this;
	}
	//CHECKSTYLE:ON

	@Override
	public String toString() {
		return "PropertyDescriptor [name=" + name + ", title=" + title
				+ ", type=" + type + ", category=" + category
				+ ", propertyLineRenderer=" + propertyLineRenderer
				+ ", contributesToCreation=" + contributesToCreation
				+ ", mandatory=" + mandatory + ", readOnly=" + readOnly
				+ ", possibleValues=" + possibleValues + ", defaultValue="
				+ defaultValue + "]";
	}	
	
}
