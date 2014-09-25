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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import mx.core.IVisualElement;
	import mx.events.PropertyChangeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.GenericMindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapNodeRendererWithDetailsCS;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	
	/**
	 * @author AlexandraTopoloaga
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapNodeRendererController extends MindMapModelRendererController {
		
		public function SampleMindMapNodeRendererController(rendererClassFactory:ClassFactoryWithConstructor, orderIndex:int = 0) {
			super(rendererClassFactory, GenericMindMapConnector, orderIndex);
		}
		
		override public function rendererModelChangedHandler(context:DiagramShellContext, renderer:IVisualElement, model:Object, event:PropertyChangeEvent):void {
			if (event != null && "details" == event.property && !(renderer is Class(getRendererClass(context, model)))) {
				// the value of this property dictates the renderer; 
				// it has been changed, and the proposed renderer class != the actual class
				context.diagramShell.shouldRefreshVisualChildren(context, context.diagramShell.rootModel);
			}
		}
	
		override public function getRendererClass(context:DiagramShellContext, model:Object):Class {
			var data:SampleMindMapModel = SampleMindMapModel(model);
			if (data.details != null && data.details != "") {
				return SampleMindMapNodeRendererWithDetailsCS;
			}
			return super.getRendererClass(context, model);
		}
		
	}
}