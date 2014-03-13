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
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.MindMapEditorFrontend;
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorProvider extends AbstractViewProvider {
				
		public static const ID:String = "mindMapEditor";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {
			var frontend:MindMapEditorFrontend = new MindMapEditorFrontend();
			frontend.editorInput = viewLayoutData.customData;
			return frontend;
		}
		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String {
			return viewLayoutData.customData;
		}
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return CorePlugin.getInstance().getResourceUrl("images/icon_flower.gif");
		}
	
	}
}