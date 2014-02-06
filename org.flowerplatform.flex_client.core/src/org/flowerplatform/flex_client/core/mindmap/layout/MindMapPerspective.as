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
package org.flowerplatform.flex_client.core.mindmap.layout {
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.layout.Perspective;
	import org.flowerplatform.flexutil.layout.SashLayoutData;
	import org.flowerplatform.flexutil.layout.StackLayoutData;
	import org.flowerplatform.flexutil.layout.WorkbenchLayoutData;

	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapPerspective extends Perspective	{
		
		public static const ID:String = "mindmapPerspective";
		
		public override function get id():String {
			return ID;
		}
		
		public override function get name():String {
			return CorePlugin.getInstance().getMessage("mindmap.perspective");
		}
		
		public override function get iconUrl():String {			
			return CorePlugin.getInstance().getResourceUrl("images/icon_flower.gif");
		}
		
		public override function resetPerspective(workbench:IWorkbench):void {
			var wld:WorkbenchLayoutData = new WorkbenchLayoutData();
			wld.direction = SashLayoutData.HORIZONTAL;
			wld.ratios = new ArrayCollection([100]);
			wld.mrmRatios = new ArrayCollection([0]);
				
			var sashEditor:SashLayoutData = addSash(wld, SashLayoutData.HORIZONTAL, [100], [0], true);
									
			load(workbench, wld, sashEditor);
		}
	}
}