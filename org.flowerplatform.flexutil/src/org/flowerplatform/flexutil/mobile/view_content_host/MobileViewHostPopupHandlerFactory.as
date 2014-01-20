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
package org.flowerplatform.flexutil.mobile.view_content_host {
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;
	
	import spark.components.Scroller;
	import spark.components.View;
	import spark.components.ViewNavigator;
	
	public class MobileViewHostPopupHandlerFactory implements IPopupHandlerFactory {
		
		public function createPopupHandler():IPopupHandler {
			return new MobileViewHostPopupHandler();
		}
		
		public function removePopup(viewContent:IVisualElement):void {
			var viewNavigator:ViewNavigator = ViewNavigator(FlexGlobals.topLevelApplication.navigator);
			var view:View = View(viewNavigator.activeView);		
			if (view is MobileViewHost && view.numElements > 0 && Scroller(view.getElementAt(0)).viewport == viewContent) {
				viewNavigator.popView();
			}
		}
		
	}
}