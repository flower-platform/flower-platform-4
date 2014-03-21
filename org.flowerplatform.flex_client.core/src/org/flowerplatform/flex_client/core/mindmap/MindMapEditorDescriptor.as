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
package org.flowerplatform.flex_client.core.mindmap {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDescriptor extends EditorDescriptor {
		
		public static const ID:String = "mindmap";
		
		override public function getEditorName():String {
			return "mindmap";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new MindMapEditorFrontend();
		}
		
		public override function getId():String {	
			return ID;
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {				
			return CorePlugin.getInstance().getResourceUrl("images/icon_flower.gif");
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {		
			if (viewLayoutData != null) {
				return viewLayoutData.customData;
			} else {
				return "mindmap";
			}
		}
	}
}