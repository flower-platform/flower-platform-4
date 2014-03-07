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
package org.flowerplatform.flex_client.properties {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Razvan Tache
	 */
	public class PropertiesViewProvider extends AbstractViewProvider {
		
		public static const ID:String = "properties";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {			
			return new PropertiesView();
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return PropertiesPlugin.getInstance().getMessage("properties.view");
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return PropertiesPlugin.getInstance().getResourceUrl("images/properties.gif");
		}
	
	}
}