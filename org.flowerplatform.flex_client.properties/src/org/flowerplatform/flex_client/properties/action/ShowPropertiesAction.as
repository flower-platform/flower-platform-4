/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.flex_client.properties.action {
	
	import org.flowerplatform.flex_client.properties.PropertiesViewProvider;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ShowPropertiesAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.properties.action.ShowPropertiesAction";
		
		public function ShowPropertiesAction(){
			super();
			label = Resources.getMessage("properties.action.show");
			icon = Resources.propertiesIcon;
			orderIndex = 1000;
		}
			
//		override public function get visible():Boolean {
//			return true;
//		}
		
		override public function run():void {	
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewIdInWorkbench(PropertiesViewProvider.ID)
				.setWidth(500)
				.setHeight(350)
				.show();
		}
	}
}