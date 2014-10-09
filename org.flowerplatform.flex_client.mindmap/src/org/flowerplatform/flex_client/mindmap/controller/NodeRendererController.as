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
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NodeRendererController extends ClassReferenceRendererController {
		
		public function NodeRendererController(rendererClassFactory:ClassFactoryWithConstructor, orderIndex:int = 0) {
			super(rendererClassFactory, orderIndex);
		}
		
		/**
		 * Return the <code>MindMapNodeWithDetailsRenderer</code> for nodes with details or notes.
		 * 
		 * @author Mariana Gheorghe
		 */
		override public function getRendererClass(context:DiagramShellContext, model:Object):Class {
//			var node:Node = Node(model);
//			if ((node.properties.hasOwnProperty(MindMapConstants.NOTE) && String(node.properties[MindMapConstants.NOTE]).length > 0) ||
//				(node.properties.hasOwnProperty(MindMapConstants.NODE_DETAILS) && String(node.properties[MindMapConstants.NODE_DETAILS]).length > 0)) {
//				return MindMapNodeWithDetailsRenderer;
//			}
			// TODO CS/MM: dezactivat pana cand o sa reintroducem nod cu details
			return super.getRendererClass(context, model);
		}
		
	}
}
