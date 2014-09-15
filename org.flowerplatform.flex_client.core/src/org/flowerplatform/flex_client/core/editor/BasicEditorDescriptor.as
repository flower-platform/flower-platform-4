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
package  org.flowerplatform.flex_client.core.editor {
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexutil.layout.ViewLayoutData;

	/**
	 * The methods for name and icon are meant to be compatible to the ones in
	 * <code>EditorDescriptor</code> which implements <code>IViewProvider</code>.
	 * 
	 * 
	 */
	public class BasicEditorDescriptor {

		/**
		 * Should return the same value as the corresponding Java <code>EditorStatefulService</code>. 
		 */
		public function getEditorName():String {
			throw new Error("This method should be implemented");
		}
		
		/**
		 * Abstract method. Called with a <code>null</code> parameter, should
		 * return the icon of the editor.
		 * 
		 * 
		 */
		public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Abstract method. Called with a <code>null</code> parameter, should
		 * return the icon of the editor.
		 * 
		 * 
		 */
		public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			throw new Error("This method should be implemented.");
		}
		
		/**
		 * Should open the corresponding editor, with the
		 * provided input. 
		 * 
		 * 
		 */
		public function openEditor(editableResourcePath:String, preferNewEditor:Boolean=false,
								   forceNewEditor:Boolean=false, openForcedByServer:Boolean=false, 
								   handleAsClientSubscription:Boolean=false, addViewInOtherStack:Boolean = false):UIComponent {
			throw new Error("This method should be implemented");
		}
		
	}
	
}
