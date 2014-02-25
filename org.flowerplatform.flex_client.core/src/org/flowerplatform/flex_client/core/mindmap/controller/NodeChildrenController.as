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
package org.flowerplatform.flex_client.core.mindmap.controller {
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.event.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeChildrenController extends ControllerBase implements IModelChildrenController {
		
		private static const EMPTY_LIST:ArrayList = new ArrayList();
		

		public function getParent(context:DiagramShellContext, model:Object):Object {
			return Node(model).parent;
		}
		
		public function getChildren(context:DiagramShellContext, model:Object):IList	{
			return EMPTY_LIST;
		}
		
		public function beginListeningForChanges(context:DiagramShellContext, model:Object):void	{	
			Node(model).addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, function (event:UpdateConnectionEndsEvent):void {updateConnectionEndsHandler(event, context);});			
		}
		
		public function endListeningForChanges(context:DiagramShellContext, model:Object):void {		
			Node(model).removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, function (event:UpdateConnectionEndsEvent):void {updateConnectionEndsHandler(event, context);});			
		}
		
		protected function updateConnectionEndsHandler(event:UpdateConnectionEndsEvent, context:DiagramShellContext):void {
			var model:Object = event.target;
			MindMapModelRendererController(context.diagramShell.getControllerProvider(model).getRendererController(model)).updateConnectors(context, model);
		}
		
	}
}