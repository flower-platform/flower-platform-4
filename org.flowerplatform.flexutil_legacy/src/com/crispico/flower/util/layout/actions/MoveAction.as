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
	import com.crispico.flower.util.layout.ArrangeTool;
	import com.crispico.flower.util.layout.Workbench;
	
	import flash.geom.Point;
	
	import flexlib.containers.SuperTabNavigator;
	
	import mx.controls.Button;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mircea Negreanu
	 */
	public class MoveAction extends WorkbenchAction {
		public static const VIEW:int=0;
		
		public static const GROUP:int=1;
		
		private var type:int;
		
		public function MoveAction(bench:Workbench, type:int) {
			super(bench);
			
			this.type = type;
			switch(type) {
				case VIEW: {
					label = UtilAssets.INSTANCE.getMessage("layout.action.move.view");
					icon = UtilAssets.INSTANCE._viewIcon;
					orderIndex = 10;
					break;
				}					
				case GROUP: {
					label = UtilAssets.INSTANCE.getMessage("layout.action.move.group");
					icon = UtilAssets.INSTANCE._viewsIcon;
					orderIndex = 20;
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
				var viewLayoutData:ViewLayoutData = selection[0];
				
				var tabNavigator:SuperTabNavigator = SuperTabNavigator(workbench.layoutDataToComponent[viewLayoutData.parent]);			
				var point:Point;
				var tab:Button;
				if (type == VIEW) {
					tab = Button(tabNavigator.getTabAt(viewLayoutData.parent.children.getItemIndex(viewLayoutData)));
					point = tab.localToGlobal(new Point(tab.stage.x + tab.width/2, tab.stage.y));
				} else {
					tab = Button(tabNavigator.getTabAt(viewLayoutData.parent.children.length - 1));
					point = tab.localToGlobal(new Point(tab.stage.x + tab.width, tab.stage.y));
					point.x += 1;
				}
				workbench.arrangeTool.startDragging(point, ArrangeTool.MOVE_BY_CLICKING);
			}
		}
	}
}