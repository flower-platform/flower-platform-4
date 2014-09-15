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
package org.flowerplatform.flex_client.core.node {
	import mx.collections.IList;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public interface IResourceOperationsHandler	{
		
		function get nodeRegistryManager():*;
		function set nodeRegistryManager(value:*):void;
		
		function updateGlobalDirtyState(someResourceNodeHasBecomeDirty:Boolean):void;
		
		function showSaveDialog(nodeRegistries:Array = null, dirtyResourceNodes:IList = null, handler:Function = null):void;
		function showReloadDialog(nodeRegistries:Array = null, resourceSets:Array = null):void;
			
	}
}
