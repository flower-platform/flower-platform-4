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
package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Vlad Bogdan Manica
	 */
	public class AddSiblingActionProvider extends AddChildActionProvider {
		
		override protected function getParent(selectedNode:Node):Node {
			return selectedNode.parent;
		}
		
		override protected function getSiblingNodeUri(selectedNode:Node):String {
			return selectedNode.nodeUri;
		}
		
	}
}