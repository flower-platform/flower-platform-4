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
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.mindmap.GenericMindMapConnector;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexdiagram.samples.mindmap.renderer.SampleMindMapNodeWithDetailsRenderer;
	
	/**
	 * @author AlexandraTopoloaga
	 */
	public class SampleMindMapNodeRendererController extends MindMapModelRendererController {
		
		public function SampleMindMapNodeRendererController(rendererClass:Class, orderIndex:int = 0) {
			super(rendererClass, GenericMindMapConnector, orderIndex);
		}
		
		/**
		 * Return the <code>SampleMindMapNodeWithDetailsRenderer</code> for nodes with details or notes.
		 * 
		 * @author Mariana Gheorghe
		 * @author AlexandraTopoloaga
		 */
		override public function getRendererClass(context:DiagramShellContext, model:Object):Class {
			var data:SampleMindMapModel = SampleMindMapModel(model);
			if ((data.note && String(data.note).length > 0) ||
				((data.details) && String(data.details).length > 0)) {
				return SampleMindMapNodeWithDetailsRenderer;
			}
			return super.getRendererClass(context, model);
		}
		
	}
}