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
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 * @author Mircea Negreanu
	 */ 
	public class MinimizeAction extends WorkbenchAction {
		public function MinimizeAction(bench:Workbench) {
			super(bench);
			
			label = UtilAssets.INSTANCE.getMessage("layout.action.minimize");
			icon = UtilAssets.INSTANCE._minimizeViewIcon;
			orderIndex = 30;
		}
		
		/**
		 * @author Cristina Constantinescu
		 * @author Mircea Negreanu
		 */
		override public function run():void {
			if (selection != null && selection.length > 0) {
				var viewLayoutData:ViewLayoutData = selection[0];
				workbench.minimize(StackLayoutData(viewLayoutData.parent));
			}
		}
	}
}