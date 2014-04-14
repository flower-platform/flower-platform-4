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

import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_STRING;

import java.util.List;

import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;

/**
 * @author Razvan Tache
 * @author Cristina Constantinescu
 * @author Sebastian Solomon
 */
public class PropertyDescriptor extends AbstractController implements IDescriptor {
	
	private String name;
	private String title;
	private String type = PROPERTY_DESCRIPTOR_TYPE_STRING;
	private String category = PROPERTY_DESCRIPTOR_DEFAULT_CATEGORY;
	private boolean contributesToCreation = false;
	private boolean mandatory = false;
	
	private boolean readOnly;
	private List<?> possibleValues;
	
	private Boolean hasChangeCheckbox = false;
	
	private Object defaultValue = null;

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public PropertyDescriptor setNameAs(String name) {
		this.name = name;
		return this;
	}
		
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PropertyDescriptor setTitleAs(String title) {
		this.title = title;
		return this;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public PropertyDescriptor setTypeAs(String type) {
		this.type = type;
		return this;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public PropertyDescriptor setCategoryAs (String category) {
		this.category = category;
		return this;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public Boolean getHasChangeCheckbox() {
		return hasChangeCheckbox;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void setHasChangeCheckbox(Boolean hasChangeCheckbox) {
		this.hasChangeCheckbox = hasChangeCheckbox;
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public PropertyDescriptor setHasChangeCheckboxAs(Boolean hasChangeCheckbox) {
		this.hasChangeCheckbox = hasChangeCheckbox;
		return this;
	}
	
	public boolean getReadOnly() {
		return readOnly;
	}
	
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public PropertyDescriptor setReadOnlyAs(boolean readOnly) {
		this.readOnly = readOnly;
		return this;
	}

	public List<?> getPossibleValues() {
		return possibleValues;
	}

	public void setPossibleValues(List<Object> possibleValues) {
		this.possibleValues = possibleValues;
	}
	
	public PropertyDescriptor setPossibleValuesAs(List<?> possibleValues) {
		this.possibleValues = possibleValues;
		return this;
	}
	
	public boolean getContributesToCreation() {
		return contributesToCreation;
	}

	public void setContributesToCreation(boolean contributeToCreation) {
		this.contributesToCreation = contributeToCreation;
	}
	
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
	
	public PropertyDescriptor setMandatoryAs(boolean mandatory) {
		this.mandatory = mandatory;
		return this;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public PropertyDescriptor setDefaultValueAs(Object defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	
	@Override
	public String toString() {
		return String.format("PropertiesDescriptor [name = %s, title = %s, category = %s, " +
				"\ntype = %s, readOnly = %b, possibleValues = %s, " +
				"\ncontributesToCreation = %b, mandatory = %b, orderIndex = %d]", 
				getName(), getTitle(), getCategory(),
				getType(), getReadOnly(), getPossibleValues(),
				getContributesToCreation(), getMandatory(), getOrderIndex());
	}
	
}
