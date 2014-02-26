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
	import flash.events.FocusEvent;
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.ControllerUtils;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.AbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexutil.text.AutoGrowTextArea;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeInplaceEditorController extends InplaceEditorController {
		
		private static const MAX_WIDTH:int = 1000;
		
		override public function canActivate(context:DiagramShellContext, model:Object):Boolean {		
			return model is Node;
		}
				
		override public function activate(context:DiagramShellContext, model:Object):void {			
			var controller:AbsoluteLayoutRectangleController = ControllerUtils.getAbsoluteLayoutRectangleController(context, model);
			var bounds:Rectangle = controller.getBounds(context, model);
		
			// create text area (auto grow width & height at CTRL + ENTER) 
			var textArea:AutoGrowTextArea = new AutoGrowTextArea();
			textArea.x = bounds.x;
			textArea.y = bounds.y;			
			textArea.minWidth = bounds.width;
			textArea.maxWidth = MAX_WIDTH; // needed for width auto grow
			textArea.minHeight = bounds.height;			
			textArea.text = Node(model).properties[NodePropertiesConstants.TEXT];			
			// set focus on text
			textArea.callLater(textArea.setFocus);
			// select all text
			textArea.addEventListener(FocusEvent.FOCUS_IN, function(event:FocusEvent):void {event.currentTarget.selectAll()});
			
			// add to diagram
			context.diagramShell.diagramRenderer.addElement(textArea);			
			context.diagramShell.modelToExtraInfoMap[model].inplaceEditor = textArea;
		}
		
		override public function commit(context:DiagramShellContext, model:Object):void {		
			var textArea:AutoGrowTextArea = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [Node(model).fullNodeId, NodePropertiesConstants.TEXT, textArea.text]);

			context.diagramShell.mainToolFinishedItsJob();
		}
		
		override public function abort(context:DiagramShellContext, model:Object):void {
			// here can be placed a warning
			context.diagramShell.mainToolFinishedItsJob();
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			var textArea:AutoGrowTextArea = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			context.diagramShell.diagramRenderer.removeElement(textArea);
			
			delete context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}
		
	}
	
}
