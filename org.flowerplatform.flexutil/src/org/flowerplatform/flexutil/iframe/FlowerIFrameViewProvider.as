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
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class FlowerIFrameViewProvider implements IViewProvider {
		
		public static const ID:String = "flowerIFrame";
		
		public function getId():String {
			return ID;
		}
		
		public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var view:IFlowerIFrame = createFlowerIFrame();
			view.url = viewLayoutData.customData;
			return UIComponent(view);
		}
		
		/**
		 * Platform-dependent.
		 */
		protected function createFlowerIFrame():IFlowerIFrame {
			if (FlexUtilGlobals.getInstance().isMobile) {
				return new StageWebViewUIComponent();
			} else {
				return new FlowerIFrame();
			}
		}
		
		public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return viewLayoutData.customData;
		}
		
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return null;
		}
		
		public function getTabCustomizer(viewLayoutData:ViewLayoutData):Object {
			return null;
		}
		
		public function getViewPopupWindow(viewLayoutData:ViewLayoutData):UIComponent {
			return null;
		}
	}
}