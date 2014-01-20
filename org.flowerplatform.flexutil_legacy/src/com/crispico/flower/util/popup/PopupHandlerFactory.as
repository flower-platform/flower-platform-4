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
package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import flash.display.DisplayObject;
	
	import mx.core.FlexGlobals;
	import mx.core.IChildList;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;
	
	/**
	 * @author Cristian Spiescu	
	 */ 
	public class PopupHandlerFactory implements IPopupHandlerFactory {
		
		/**		 
		 * @author Cristina Constantinescu
		 */ 
		public function createPopupHandler():IPopupHandler {
			return new PopupHandler();
		}
		
		public function removePopup(viewContent:IVisualElement):void {
			var popup:UIComponent;
			// from what I have seen, popups can be located in both locations
			popup = iteratePopups(FlexGlobals.topLevelApplication.systemManager, viewContent);
			if (popup == null) {
				popup = iteratePopups(FlexGlobals.topLevelApplication.systemManager.popUpChildren, viewContent);
			}
			if (popup != null) {
				PopUpManager.removePopUp(popup);
			}
		}
		
		/**		 
		 * @author Cristina Constantinescu
		 */
		private function iteratePopups(childList:IChildList, viewContent:IVisualElement):UIComponent {
			for (var i:int = 0; i < childList.numChildren; i++) {
				var child:DisplayObject = childList.getChildAt(i);
				if (!(child is ResizablePopupWindowViewHost || child is ModalSpinner || child is ViewPopupWindowViewHost) || !UIComponent(child).isPopUp) {
					continue;
				}
				if (child is ResizablePopupWindowViewHost || child is ViewPopupWindowViewHost) {
					var d:UIComponent = UIComponent(child);
					if (d.numChildren == 0 || !(d.getChildAt(0) == viewContent)) {
						continue;
					}
					return d;	
				} else {
					// ModalSpinner
					if (ModalSpinner(child).childrenUnderSpinner.length > 0 && ModalSpinner(child).childrenUnderSpinner[0] == viewContent) {
						return ModalSpinner(child);
					}
				}
			}
			return null;
		}
	}
}