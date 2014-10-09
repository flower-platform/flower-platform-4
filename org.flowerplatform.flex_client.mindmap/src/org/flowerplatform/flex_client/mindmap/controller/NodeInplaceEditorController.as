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
package org.flowerplatform.flex_client.mindmap.controller {
	import flash.display.DisplayObject;
	import flash.events.FocusEvent;
	import flash.geom.Rectangle;
	
	import mx.core.UIComponent;
	
	import spark.components.RichText;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexdiagram.mindmap.IAbstractMindMapModelRenderer;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.InplaceEditorController;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
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
			var renderer:IAbstractMindMapModelRenderer = IAbstractMindMapModelRenderer(context.diagramShell.getRendererForModel(context, model));
			var rendererLabelDisplay:RichText = renderer.getLabelDisplay();
			var bounds:Rectangle = rendererLabelDisplay.getBounds(DisplayObject(context.diagramShell.diagramRenderer));
			
			// create text area (auto grow width & height at CTRL + ENTER) 
			var textArea:AutoGrowTextArea = new AutoGrowTextArea();
			textArea.depth = int.MAX_VALUE; // model has depth, so put ied above
			textArea.x = bounds.x;
			textArea.y = bounds.y;			
			textArea.minWidth = bounds.width;
			textArea.maxWidth = UIComponent(renderer).maxWidth; // needed for width auto grow
			textArea.minHeight = bounds.height;			
						
			// get styles from node's labelDisplay renderer
			textArea.setStyle("fontFamily", rendererLabelDisplay.getStyle("fontFamily"));
			textArea.setStyle("fontSize", rendererLabelDisplay.getStyle("fontSize"));
			textArea.setStyle("fontWeight", rendererLabelDisplay.getStyle("fontWeight"));
			textArea.setStyle("fontStyle", rendererLabelDisplay.getStyle("fontStyle"));
			textArea.setStyle("color", rendererLabelDisplay.getStyle("color"));
			
			textArea.text = CorePlugin.getInstance().getNodeValuesProviderForMindMap(context.diagramShell.registry, Node(model))
					.getValue(context.diagramShell.registry, Node(model), FlexDiagramConstants.BASE_RENDERER_TEXT) as String;		
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
			var valuesProvider:ValuesProvider = CorePlugin.getInstance().getNodeValuesProviderForMindMap(context.diagramShell.registry, Node(model));

			if (valuesProvider.getValue(context.diagramShell.registry, Node(model), FlexDiagramConstants.BASE_RENDERER_TEXT) != textArea.text) {
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [Node(model).nodeUri, 
					valuesProvider.getPropertyName(context.diagramShell.registry, Node(model), FlexDiagramConstants.BASE_RENDERER_TEXT), textArea.text], function(data:Object):void {
						context.diagramShell.mainToolFinishedItsJob();
					});
			} else {
				context.diagramShell.mainToolFinishedItsJob();
			}
		}
		
		override public function deactivate(context:DiagramShellContext, model:Object):void {
			var textArea:AutoGrowTextArea = context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;
			context.diagramShell.diagramRenderer.removeElement(textArea);
			
			delete context.diagramShell.modelToExtraInfoMap[model].inplaceEditor;		
			
			DiagramRenderer(context.diagramShell.diagramRenderer).setFocus();
		}
		
	}
	
}
