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
package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.DiagramShellAwareActionBase;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowOrderedMatchesAction extends DiagramShellAwareActionBase {
		
		public function ShowOrderedMatchesAction() {
			super();
			label = Resources.getMessage("regex.action.ordered");
			icon = Resources.brickIcon;
		}
		
		override public function get visible():Boolean {			
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			return obj is Node && Node(obj).type == CodeSyncRegexConstants.REGEX_MATCHES_TYPE;
		}
		
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var ds:MindMapEditorDiagramShell = MindMapEditorDiagramShell(diagramShell);
			if (ds.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node)) {
				CorePlugin.getInstance().nodeRegistryManager.collapse(ds.nodeRegistry, node);
			}			
			CorePlugin.getInstance().nodeRegistryManager.expand(ds.nodeRegistry, node, getExpandContext());
		}
			
		protected function getExpandContext():Object {
			return new Object();
		}
		
	}
}