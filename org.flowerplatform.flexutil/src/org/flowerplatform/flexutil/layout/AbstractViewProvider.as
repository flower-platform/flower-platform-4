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
package org.flowerplatform.flexutil.layout {
	
	import mx.core.UIComponent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AbstractViewProvider implements IViewProvider {
				
		public function getId():String {
			throw new Error("This method needs to be implemented.");
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			throw new Error("This method needs to be implemented.");
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
		
	}
}