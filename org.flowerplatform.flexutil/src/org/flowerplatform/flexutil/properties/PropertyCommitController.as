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
package org.flowerplatform.flexutil.properties {
	import org.flowerplatform.flexutil.controller.AbstractController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class PropertyCommitController extends AbstractController {
		public function PropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		protected function getTarget(propertyEntry:PropertyEntry):Object {
			var target:Object;
			if (propertyEntry.eventDispatcher != null) {
				target = propertyEntry.eventDispatcher;
			} else {
				target = propertyEntry.model;
			}
			return target;
		}
		
		public function commitProperty(propertyEntry:PropertyEntry):void {
			var target:Object = getTarget(propertyEntry);
			target[propertyEntry.descriptor.name] = propertyEntry.value;
		}
		
		public function unsetProperty(propertyEntry:PropertyEntry):void {
			var target:Object = getTarget(propertyEntry);
			delete target[propertyEntry.descriptor.name];
		}
	}
}