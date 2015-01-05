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
package org.flowerplatform.flex_client.mindmap {
	import flash.events.IEventDispatcher;
	
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeValuesProvider extends ValuesProvider {
		public function NodeValuesProvider(featurePrefix:String="", orderIndex:int=0) {
			super(featurePrefix, orderIndex);
		}
		
		override public function getValueFromActualPropertyName(object:IEventDispatcher, actualPropertyName:String):Object {
			return Node(object).getPropertyValue(actualPropertyName);
		}
		
		override public function getActualObject(object:IEventDispatcher):IEventDispatcher {
			return IEventDispatcher(Node(object).properties);
		}
		
	}
}