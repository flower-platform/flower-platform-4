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
package com.crispico.flower.util.layout.actions {
	import com.crispico.flower.util.UtilAssets;
	import com.crispico.flower.util.layout.Workbench;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mircea Negreanu
	 */
	public class CloseAction extends WorkbenchAction {
		public static const CLOSE:int = 0;
		
		public static const CLOSE_ALL:int = 1;
		
		public static const CLOSE_OTHERS:int = 2;
		
		private var type:int;
		
		public function CloseAction(bench:Workbench, type:int) {
			super(bench);
			
			this.type = type;
			switch(type) {
				case CLOSE:	{
					label = UtilAssets.INSTANCE.getMessage("layout.action.close");
					icon = UtilAssets.INSTANCE._closeTabIcon;
					orderIndex = 50;
					break;
				}
				case CLOSE_ALL:	{
					label = UtilAssets.INSTANCE.getMessage("layout.action.closeAll");
					icon = UtilAssets.INSTANCE._closeAllViewIcon;
					orderIndex = 60;
					break;
				}
				case CLOSE_OTHERS:	{
					label = UtilAssets.INSTANCE.getMessage("layout.action.closeOthers");
					icon = UtilAssets.INSTANCE._closeViewIcon;
					orderIndex = 70;
					break;
				}
			}	
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Mircea Negreanu
		 */
		override public function run():void {
			if (selection != null && selection.length > 0) {
				var viewLayoutData:ViewLayoutData = ViewLayoutData(selection[0]);
				var views:ArrayCollection = new ArrayCollection();
				switch(type) {
					case CLOSE:	{
						views.addItem(viewLayoutData);
						break;
					}
					case CLOSE_ALL:	{
						views = viewLayoutData.parent.children;
						for each (var child:ViewLayoutData in views) {
							if (workbench.activeViewList.getActiveView() == workbench.layoutDataToComponent[child]) {
								workbench.activeViewList.removeActiveView(false);
								break;
							}
						}
						
						break;
					}
					case CLOSE_OTHERS:	{
						for each (var child:ViewLayoutData in viewLayoutData.parent.children) {
							if (child != viewLayoutData) {
								views.addItem(child);
							}
						}
						break;
					}
				}
				var components:ArrayCollection = new ArrayCollection();			
				for each (var view:ViewLayoutData in views) {
					components.addItem(workbench.layoutDataToComponent[view]);							
				}
				workbench.closeViews(components);
			}
		}
	}
}