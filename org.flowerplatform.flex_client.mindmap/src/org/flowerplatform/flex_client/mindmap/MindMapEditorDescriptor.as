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
package org.flowerplatform.flex_client.mindmap {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return MindMapConstants.MINDMAP_EDITOR_NAME;
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new MindMapEditorFrontend();
		}
		
		public override function getId():String {	
			return MindMapConstants.MINDMAP_CONTENT_TYPE;
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {				
			return Resources.flowerIcon;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {		
			if (viewLayoutData != null) {
				return viewLayoutData.customData;
			} else {
				return MindMapConstants.MINDMAP_EDITOR_NAME;
			}
		}
	}
}