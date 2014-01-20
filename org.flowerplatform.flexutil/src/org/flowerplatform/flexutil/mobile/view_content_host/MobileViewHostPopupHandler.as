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
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
		
	/**
	 * @author Cristian Spiescu
	 */ 
	public class MobileViewHostPopupHandler implements IPopupHandler {
		
		protected var _viewContent:IViewContent;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		protected var _viewIdInWorkbench:String;
		
		public function setTitle(value:String):IPopupHandler {
			return this;
		}
		
		public function setWidth(value:Number):IPopupHandler {
			return this;
		}
		
		public function setHeight(value:Number):IPopupHandler {
			return this;
		}
		
		public function setViewContent(value:IViewContent):IPopupHandler {
			_viewContent = value;
			return this;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		public function setViewIdInWorkbench(value:String):IPopupHandler {
			_viewIdInWorkbench = value;
			return this;
		}	
		
		public function show(modal:Boolean=true):void {
			showInternal(false);
		}
		
		public function showModalOverAllApplication():void {
			showInternal(true);		
		}
		
		/**
		 * @author Cristian Spiescu
		 * @author Cristina Constantinescu
		 */ 
		private function showInternal(modalOverAllApplication:Boolean):void {
			if (_viewIdInWorkbench) {
				// create view content from viewId
				var component:UIComponent = FlexUtilGlobals.getInstance().composedViewProvider.createView(new ViewLayoutData(_viewIdInWorkbench));
				if (!(component is IViewContent)) {
					throw new Error("The view graphical component should be an IViewContent!");
				}
				_viewContent = IViewContent(component);
			}
			
			FlexGlobals.topLevelApplication.navigator.pushView(MobileViewHost, { 
				viewContent: _viewContent,
				popupHandler: this, 
				modalOverAllApplication: modalOverAllApplication
			});
		}
		
	}
}