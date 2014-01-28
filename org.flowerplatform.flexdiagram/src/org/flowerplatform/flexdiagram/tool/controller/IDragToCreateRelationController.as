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
package org.flowerplatform.flexdiagram.tool.controller {
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface IDragToCreateRelationController {
		
		function activate(model:Object):void;
		function drag(model:Object, deltaX:Number, deltaY:Number):void;
		function drop(sourceModel:Object, targetModel:Object):void;
		function deactivate(model:Object):void;
		
	}	
}