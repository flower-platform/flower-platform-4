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
package org.flowerplatform.flexdiagram.mindmap {
	import mx.collections.ArrayList;
	import mx.utils.object_proxy;
	
	/**
	 * Wrapper for the <code>rootModel</code> of a <code>MindMapDiagramShell</code>.
	 * 
	 * <p>
	 * Stores:
	 * <ul>
	 * 	<li> the initial model given as rootModel
	 * 	<li> the list of visual children displayed as children for <code>DiagramRenderer</code>.
	 * </ul>
	 * 
	 * @see MindMapRootModelChildren.getChildren(model)
	 * @see MindMapDiagramShell.set rootModel(value)
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class MindMapRootModelWrapper {
		
		public static const ID:String = "mindMapRootModelWrapper";
		
		public var model:Object;
		
		public var children:ArrayList;
		
		public function MindMapRootModelWrapper(model:Object) {
			this.model = model;	
		}
		
	}
}