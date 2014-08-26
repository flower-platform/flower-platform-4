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
package org.flowerplatform.flex_client.mindmap.action {
	
	import flash.ui.Keyboard;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexutil.shortcut.Shortcut;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeUpAction extends AbstractNavigateAction {
		
		public static const ID:String = "org.flowerplatform.flex_client.mindmap.action.NodeUpAction";
		
		public function NodeUpAction(appendNodesToCurrentSelection:Boolean = false) {
			super(Resources.getMessage("mindmap.up"), new Shortcut(false, appendNodesToCurrentSelection, false, Keyboard.UP), appendNodesToCurrentSelection);			
		}
		
		override protected function getNodes(node:Node, context:DiagramShellContext):Array {
			return getNodes_navigateUpDown(node, context, false, false);
		}

	}
}