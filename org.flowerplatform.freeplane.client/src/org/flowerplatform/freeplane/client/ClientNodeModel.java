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
package org.flowerplatform.freeplane.client;

import org.flowerplatform.js_client.java.node.ClientNode;
import org.freeplane.core.extension.IExtension;

/**
 * @author Valentina Bojan
 */
public class ClientNodeModel implements IExtension {

	private ClientNode node;
	
	public ClientNode getNode() {
		return node;
	}
	
	public void setNode(ClientNode node) {
		this.node = node;
	}
	
}
