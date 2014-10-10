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
package org.flowerplatform.flexutil.iframe {
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	public class IFrameOpenUrlAction extends ActionBase {
		
		override public function run():void {
			var view:IFrameOpenUrlView = new IFrameOpenUrlView();
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()			
				.setViewContent(view)
				.setTitle(label)
				.setIcon(icon)
				.setWidth(350)
				.setHeight(150)
				.show();
		}
	}
}