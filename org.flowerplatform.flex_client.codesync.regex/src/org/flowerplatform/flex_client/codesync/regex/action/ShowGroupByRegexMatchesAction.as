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
	public class ShowGroupByRegexMatchesAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.ShowGroupByRegexMatchesAction";
		
		public function ShowGroupByRegexMatchesAction() {
			super();			
			isToggleAction = true;
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.action.grouped");	
		}
				
		override public function run():void {
			super.run();
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			var node:Node = Node(obj);
			
			var ds:MindMapEditorDiagramShell = MindMapEditorDiagramShell(diagramShell);
			if (ds.getModelController(diagramShellContext, node).getExpanded(diagramShellContext, node)) {
				CorePlugin.getInstance().nodeRegistryManager.collapse(ds.nodeRegistry, node);
			}			
						
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.setProperty", 
				[node.nodeUri, CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX, isSelected], 
				function ():void {					
					CorePlugin.getInstance().nodeRegistryManager.expand(ds.nodeRegistry, node, new Object());					
				}
			);
		}
		
	}
}