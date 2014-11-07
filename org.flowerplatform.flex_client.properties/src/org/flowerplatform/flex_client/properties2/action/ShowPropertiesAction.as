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
package org.flowerplatform.flex_client.properties2.action {
	
	import org.flowerplatform.flex_client.properties2.PropertiesViewProvider;
	import org.flowerplatform.flex_client.resources.ActionOrderIndexes;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.layout.ShowViewAction;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ShowPropertiesAction extends ShowViewAction {
		
		public static const ID:String = "ShowPropertiesAction";
		
		public function ShowPropertiesAction(){
			super();
			label = Resources.getMessage("properties.action.show");
			icon = Resources.propertiesIcon;
			viewId = PropertiesViewProvider.ID;
			width = 500;
			height = 350;
			orderIndex = ActionOrderIndexes.PROPERTIES;
		}
		
	}
}