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
package org.flowerplatform.flexdiagram.controller.model_children {
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShellContext;

	/**
	 * Should be provided even by elements that have no children, if they are connectable
	 * with connectors!
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IModelChildrenController {
		function getParent(context:DiagramShellContext, model:Object):Object;
		function getChildren(context:DiagramShellContext, model:Object):IList;
		function beginListeningForChanges(context:DiagramShellContext, model:Object):void;
		function endListeningForChanges(context:DiagramShellContext, model:Object):void;
	}
}