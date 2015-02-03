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
package org.flowerplatform.js_client.java.node;

import java.util.Map;

import org.flowerplatform.core.node.remote.Node;
import org.mozilla.javascript.NativeObject;

/**
 * @author Cristina Constantinescu
 */
public class ClientNode extends Node {

	private ClientNode parent;
	
	/**
	 * public attribute -> compatible with javascript code
	 * Otherwise we must create getter/setter for it and this will override the Node's getter/setter for its properties attribute.
	 */
	public NativeObject properties;
					
	public ClientNode() {
		super();
		this.properties = new NativeObject();
	}

	public void setProperties(Map<String, Object> properties) {	
		super.setProperties(properties);
		NativeObject nobj = new NativeObject();
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			nobj.defineProperty(entry.getKey(), entry.getValue(), NativeObject.EMPTY);
		}
		this.properties = nobj;
	}
	
	public Object getPropertyValue(String property) {		
		return properties.get(property);
	}
	
	public ClientNode getParent() {
		return parent;
	}

	public void setParent(ClientNode parent) {
		this.parent = parent;
	}
	
	public boolean equals(Object obj) {
		return (this == obj);
	}
	
	public native int hashCode();

}
