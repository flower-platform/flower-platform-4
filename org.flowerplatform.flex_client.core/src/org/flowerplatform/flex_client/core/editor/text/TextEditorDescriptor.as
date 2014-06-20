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
package org.flowerplatform.flex_client.core.editor.text {
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.editor.EditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class TextEditorDescriptor extends EditorDescriptor {
		
		override public function getEditorName():String {
			return "text";
		}
		
		override protected function createViewInstance():EditorFrontend	{
			return new TextEditorFrontend();
		}
		
		public override function getId():String {	
			return CoreConstants.TEXT_CONTENT_TYPE;
		}
		
		public override function getIcon(viewLayoutData:ViewLayoutData=null):Object {				
			return null;
		}
		
		public override function getTitle(viewLayoutData:ViewLayoutData=null):String {		
			return viewLayoutData.customData;
		}
	}
}