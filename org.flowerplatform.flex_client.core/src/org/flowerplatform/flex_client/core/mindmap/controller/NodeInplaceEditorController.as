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
	import flash.display.DisplayObject;
	import flash.events.FocusEvent;
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.NodePropertiesConstants;
	import org.flowerplatform.flex_client.core.mindmap.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	import org.flowerplatform.flexutil.text.AutoGrowSkinnableTextBaseSkin;
	import org.flowerplatform.flexutil.text.AutoGrowTextArea;
	
	import spark.components.TextInput;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeInplaceEditorController extends ControllerBase implements IInplaceEditorController {
		
		private static const MAX_WIDTH:int = 1000;
		
		public function NodeInplaceEditorController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function canActivate(model:Object):Boolean {		
			return model is Node;
		}
				
		public function activate(model:Object):void {			
			var controller:IAbsoluteLayoutRectangleController = diagramShell.getControllerProvider(model).getAbsoluteLayoutRectangleController(model);
			var bounds:Rectangle = controller.getBounds(model);
			
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
			diagramShell.diagramRenderer.addElement(textArea);			
			diagramShell.modelToExtraInfoMap[model].inplaceEditor = textArea;
		}
		
		public function commit(model:Object):void {		
			var textArea:AutoGrowTextArea = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [Node(model).fullNodeId, NodePropertiesConstants.TEXT, textArea.text]);

			diagramShell.mainToolFinishedItsJob();
		}
		
		public function abort(model:Object):void {
			// here can be placed a warning
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			var textArea:AutoGrowTextArea = diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			diagramShell.diagramRenderer.removeElement(textArea);
			
			delete diagramShell.modelToExtraInfoMap[model].inplaceEditor;			
		}
		
	}
	
}
