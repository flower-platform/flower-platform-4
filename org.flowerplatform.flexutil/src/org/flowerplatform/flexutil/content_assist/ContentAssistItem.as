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
package org.flowerplatform.flexutil.content_assist {
	
	/**
	 * @author Mariana Gheorghe
	 */
	[RemoteClass(alias="org.flowerplatform.editor.model.ContentAssistItem")]
	public class ContentAssistItem {
		
		public var item:Object;

		/**
		 * Used as the main string of the item renderer.
		 */
		public var mainString:String;
		
		/**
		 * Used as the extra string of the item renderer (optional).
		 */
		public var extraString:String; 
		
		/**
		 * Used to retrieve the icon.
		 */
		public var iconUrl:String;
		
	}
}