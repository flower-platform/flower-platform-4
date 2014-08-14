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
package com.crispico.flower.util.popup {
	
	import com.crispico.flower.util.layout.Workbench;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.WorkbenchLayoutData;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.spinner.ModalSpinner;
	import org.flowerplatform.flexutil.spinner.ModalSpinnerViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PopupHandler implements IPopupHandler {
				
		private var _height:Number;
		private var _width:Number;
		private var _title:String;
		private var _icon:Object;
		private var _viewContent:IViewContent;
		private var _viewIdInWorkbench:String;
		
		public function setHeight(value:Number):IPopupHandler {
			_height = value;
			return this;
		}
		
		public function setWidth(value:Number):IPopupHandler {
			_width = value;
			return this;
		}	
		
		public function setTitle(value:String):IPopupHandler {
			_title = value;
			return this;
		}
		
		public function setIcon(value:Object):IPopupHandler {
			_icon = value;
			return this;
		}
		
		public function setViewContent(value:IViewContent):IPopupHandler {
			_viewContent = value;
			return this;
		}
			
		public function setViewIdInWorkbench(value:String):IPopupHandler {
			_viewIdInWorkbench = value;
			return this;
		}
		
		public function show(modal:Boolean=true):void {
			if (_viewIdInWorkbench != null) {			
				var workbench:Workbench = Workbench(FlexUtilGlobals.getInstance().workbench);
				var undockedViews:ArrayCollection = WorkbenchLayoutData(workbench.rootLayout).undockedViews;
				for each (var view:Object in undockedViews) {
					if (ViewLayoutData(view).viewId == _viewIdInWorkbench) {
						// view already open as undocked
						return;
					}
				}
				// verify if viewId component already exists on workbench; if true, reuse component
				var component:UIComponent = workbench.getComponentById(String(_viewIdInWorkbench));
				if (component != null) {
					workbench.closeView(component, false, false);
				}				
				workbench.addViewInPopupWindow(_viewIdInWorkbench, NaN, NaN, _width, _height, false, component, new ViewPopupWindowViewHost());		
			} else {
				var resizablePopup:ResizablePopupWindowViewHost = new ResizablePopupWindowViewHost(_viewContent);
				
				resizablePopup.title = _title;
				resizablePopup.setIcon(_icon);
				if (!isNaN(_height)) {
					resizablePopup.height = _height;
				}
				if (!isNaN(_width)) {
					resizablePopup.width = _width;
				}						
				resizablePopup.showPopup(NaN, NaN, null, modal);
			}	
		}
		
		public function showModalOverAllApplication():void {
			var modalSpinner:ModalSpinner = new ModalSpinnerViewHost(_viewContent);
			ModalSpinner.addGlobalModalSpinner(null, modalSpinner);
		}
		
	}
}
