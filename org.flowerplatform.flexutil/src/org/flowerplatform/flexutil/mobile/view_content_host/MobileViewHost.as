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
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionUtil;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.Button;
	import spark.components.Group;
	import spark.components.Label;
	import spark.components.Scroller;
	import spark.components.View;
	import spark.core.IViewport;
	import spark.primitives.BitmapImage;
	
	/**
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public class MobileViewHost extends MobileViewHostBase {
		
		override protected function createChildren():void {
			super.createChildren();
			
			var scroller:Scroller = new Scroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			addElement(scroller);
			
			setActiveViewContent(IViewContent(data.viewContent));
			activeViewContent.viewHost = this;
			activeViewContent.percentHeight = 100;
			activeViewContent.percentWidth = 100;
			
			if (activeViewContent is IViewport) {
				scroller.viewport = IViewport(activeViewContent);
			} else { // the content cannot be set as scroller's viewport, so wrap it around a group
				var wrapper:Group = new Group();
				wrapper.percentHeight = 100;
				wrapper.percentWidth = 100;
				wrapper.addElement(activeViewContent);
				scroller.viewport = wrapper;
			}
			
			if (data.modalOverAllApplication) {
				navigationContent = [];
			}
		}
		
	}
}