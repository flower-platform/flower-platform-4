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
package org.flowerplatform.flexdiagram.samples.controller {
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;
	import org.flowerplatform.flexdiagram.controller.model_children.ModelChildrenController;
	import org.flowerplatform.flexdiagram.samples.model.BasicSubModel;
	
	public class BasicSubModelChildrenProvider extends ModelChildrenController {

		private const children:ArrayList = new ArrayList();
		
		override public function getParent(context:DiagramShellContext, model:Object):Object {
			return BasicSubModel(model).parent;
		}
		
		override public function getChildren(context:DiagramShellContext, model:Object):IList {
			return children;
		}
		
		override public function beginListeningForChanges(context:DiagramShellContext, model:Object):void {
		}
		
		override public function endListeningForChanges(context:DiagramShellContext, model:Object):void {
		}
	}
}