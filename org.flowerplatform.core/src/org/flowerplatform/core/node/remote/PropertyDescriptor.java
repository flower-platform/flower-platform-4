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
	private boolean contributeToCreation = false;
	private boolean isMandatory = false;
	
	private boolean readOnly;
	private List<?> possibleValues;
	
	private Boolean hasChangeCheckbox = false;

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
	
	public boolean isContributeToCreation() {
		return contributeToCreation;
	}

	public void setContributeToCreation(boolean contributeToCreation) {
		this.contributeToCreation = contributeToCreation;
	}
	
	public PropertyDescriptor setContributeToCreationAs(boolean contributeToCreation) {
		this.contributeToCreation = contributeToCreation;
		return this;
	}

	public boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	public PropertyDescriptor setIsMandatoryAs(boolean isMandatory) {
		this.isMandatory = isMandatory;
		return this;
	}
	
}
