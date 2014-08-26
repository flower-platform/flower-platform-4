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
package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.IEditorFrontendAware;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * Convenience class for actions that are <code>IEditorFrontendAware</code>.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class EditorFrontendAwareAction extends ActionBase implements IEditorFrontendAware {
		
//		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.EditorFrontendAwareAction";
		
		private var _editorFrontend:EditorFrontend;
		
		public function set editorFrontend(value:EditorFrontend):void {
			_editorFrontend = value;
		}
		
		public function get editorFrontend():EditorFrontend {
			return _editorFrontend;
		}
	}
}