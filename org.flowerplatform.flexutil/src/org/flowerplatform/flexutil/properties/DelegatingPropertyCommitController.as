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
	import org.flowerplatform.flexutil.FlexUtilConstants;

	/**
	 * @author Cristian Spiescu
	 */
	public class DelegatingPropertyCommitController extends PropertyCommitController {
		public function DelegatingPropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function commitProperty(propertyEntry:PropertyEntry):void {
			var delegate:PropertyCommitController = PropertyCommitController(propertyEntry.typeDescriptorRegistry.getSingleController(FlexUtilConstants.FEATURE_PROPERTY_COMMIT_CONTROLLER, propertyEntry.model));
			if (delegate != null) {
				delegate.commitProperty(propertyEntry);
			}
		}
		
	}
}