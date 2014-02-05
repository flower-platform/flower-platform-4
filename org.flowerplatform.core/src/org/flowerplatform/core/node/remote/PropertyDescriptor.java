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

/**
 * @author Razvan Tache
 * @author Cristina Constantinescu
 */
public class PropertyDescriptor {
	
	private static final String DEFAULT_TYPE = "String";
	
	private String name;
	private String type = DEFAULT_TYPE;
	private boolean readOnly = true;
		
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
		
}
