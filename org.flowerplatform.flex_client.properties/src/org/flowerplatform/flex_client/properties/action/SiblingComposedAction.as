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
package org.flowerplatform.flex_client.properties.action
{
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ComposedAction;

	public class SiblingComposedAction extends ComposedAction {
		
		public static const ID:String = "org.flowerplatform.flex_client.properties.action.SiblingComposedAction";
		
		public function SiblingComposedAction() {
			label = Resources.getMessage("action.newSibling");	
			icon = Resources.addIcon;
			orderIndex = 15;
			delegateIfSingleChild = true;			
		}
	}
}