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
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.SampleMindMapModel;
	import org.flowerplatform.flexutil.samples.properties.SamplePropertiesHelper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class SampleMindMapModelChildrenController extends ModelChildrenController {
		
		override public function getParent(context:DiagramShellContext, model:Object):Object {
			return SampleMindMapModel(model).parent;
		}
		
		override public function getChildren(context:DiagramShellContext, model:Object):IList	{
			// no children; this controller is used only to dispatch events
//			return EmptyList.INSTANCE;
			return new SamplePropertiesHelper().getPropertyEntries(null, context.diagramShell.registry, model);
		}
		
	}
}