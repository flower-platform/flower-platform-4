/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.core.node.controller {
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelController;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class DebugControllers {

		protected function addNodeController(type:String, controller:MindMapModelController):void {
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(type)
				.addSingleController(FlexDiagramConstants.MINDMAP_MODEL_CONTROLLER, controller);
		}
		
		protected function createNode(type:String, ssp:String, fragment:String, name:String, icons:String = null, hasChildren:Boolean = true):Node {
			var node:Node = new Node(CoreConstants.VIRTUAL_NODE_SCHEME + ":" + ssp + "#" + fragment);
			node.type = type;			
			node.properties[CoreConstants.NAME] = name;
			node.properties[CoreConstants.ICONS] = icons;
			node.properties[CoreConstants.HAS_CHILDREN] = hasChildren;
			return node;
		}
		
	}
}