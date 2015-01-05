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
package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexPlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.IEditorFrontendAware;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.MultipleSelectionActionBase;
		
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowTextEditorAction extends MultipleSelectionActionBase implements IEditorFrontendAware {
		
		private var _editorFrontend:EditorFrontend;
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.ShowTextEditorAction";
		
		public function ShowTextEditorAction() {
			super();
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.showTextEditor");
			icon = Resources.fileIcon;
		}	
				
		public function set editorFrontend(value:EditorFrontend):void {
			_editorFrontend = value;
		}
		
		public function get editorFrontend():EditorFrontend {			
			return _editorFrontend;
		}		
				
		override public function run():void	{
			CodeSyncRegexPlugin.getInstance().getTextEditorFrontend(editorFrontend, true);
		}
		
	}
}
