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
package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.renderer.ClassReferenceRendererController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.ClassFactoryWithConstructor;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author AlexandraTopoloaga
	 * @author Cristian Spiescu
	 */
	public class SampleMindMapModelRendererController extends ClassReferenceRendererController {
		
		protected var cachedContext:DiagramShellContext;
		
		public function SampleMindMapModelRendererController(rendererClassFactory:ClassFactoryWithConstructor, orderIndex:int = 0) {
			super(rendererClassFactory, orderIndex);
			removeRendererIfModelIsDisposed = true;
		}
		
		override public function getUniqueKeyForRendererToRecycle(context:Object, model:Object):Object {
			var result:String = Utils.getClassNameForObject(getRendererClass(context, model), false);
			if (SampleMindMapModel(model).details != null && SampleMindMapModel(model).details != "") {
				result += ".hasDetails";
			}
			if (SampleMindMapModel(model).showProperties) {
				result += ".hasProperties";
			}
			return result;
		}
		
	}
}