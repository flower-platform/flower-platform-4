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
	
	import mx.messaging.messages.ErrorMessage;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorDiagramShell;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeController;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.renderer.IDiagramShellAware;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ReloadAction extends DiagramShellAwareActionBase {
		
		public function ReloadAction() {			
			label = CorePlugin.getInstance().getMessage("mindmap.action.reload");
			preferShowOnActionBar = true;
			orderIndex = 100;
		}
				
		override public function get visible():Boolean {			
			return true;
		}
		
		private function reloadCallbackHandler(result:ResultEvent):void {			
			MindMapEditorDiagramShell(diagramShell).updateProcessor.requestChildren(null);
		}
		
		override public function run():void {
			CorePlugin.getInstance().serviceLocator.invoke("freeplaneService.load", null, reloadCallbackHandler);			
		}			
		
	}
}
