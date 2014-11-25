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
package org.flowerplatform.flex_client.properties2 {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flexutil.properties.PropertyCommitController;
	import org.flowerplatform.flexutil.properties.PropertyEntry;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodePropertyCommitController extends PropertyCommitController {
		public function NodePropertyCommitController(orderIndex:int=0) {
			super(orderIndex);
		}
		
		override public function commitProperty(propertyEntry:PropertyEntry):void {
			// I'm not sure if it's OK to do this. On one hand: the UI is updated immediately; but on
			// the other hand: maybe the user deletes a property that's not removable; the server wouldn't allow
			// this, but on the client, the property is already removed. Idem for setting with a wrong value
			super.commitProperty(propertyEntry);
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.setProperty", 
				[Node(propertyEntry.model).nodeUri, propertyEntry.descriptor.name, propertyEntry.value]);
		}
		
		override public function unsetProperty(propertyEntry:PropertyEntry):void {
			// see comment below
			super.unsetProperty(propertyEntry);
			CorePlugin.getInstance().serviceLocator.invoke(
				"nodeService.unsetProperty", 
				[Node(propertyEntry.model).nodeUri, propertyEntry.descriptor.name]);
		}
		
	}
}