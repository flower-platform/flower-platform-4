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
package org.flowerplatform.flex_client.core.mindmap.action {
	
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ReloadAction extends ActionBase {
		
		private var editorFrontend:MindMapEditorFrontend;
		
		public function ReloadAction(editorFrontend:MindMapEditorFrontend) {
			this.editorFrontend = editorFrontend;
			label = CorePlugin.getInstance().getMessage("mindmap.action.reload");
			preferShowOnActionBar = true;
		}
				
		override public function get visible():Boolean {			
			return true;
		}
		
		override public function run():void {
			CorePlugin.getInstance().mindMapService.reload(reloadCallbackHandler);
		}
		
		private function reloadCallbackHandler(result:ResultEvent):void {
			var diagramShell:MindMapDiagramShell = MindMapDiagramShell(editorFrontend.diagramShell);
			
			// TODO CC: temporary code (to be refactored when update mechanism implemented)
			var rootModel:Object = diagramShell.getControllerProvider(diagramShell.rootModel).getModelChildrenController(diagramShell.rootModel).getChildren(diagramShell.rootModel).getItemAt(0);
			NodeController(IMindMapControllerProvider(diagramShell.getControllerProvider(rootModel)).getMindMapModelController(rootModel)).disposeModel(rootModel);
			
			editorFrontend.requestRootModel();
		}
		
	}
}