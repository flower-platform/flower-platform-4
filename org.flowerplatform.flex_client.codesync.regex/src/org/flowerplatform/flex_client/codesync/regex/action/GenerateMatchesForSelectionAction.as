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
package org.flowerplatform.flex_client.codesync.regex.action {
	import mx.collections.IList;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Elena Posea 
	 */
	public class GenerateMatchesForSelectionAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.GenerateMatchesForSelectionAction";
				
		public function GenerateMatchesForSelectionAction() {
			super();
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.generateForSelection");
			icon = Resources.reloadIcon;
		}
		
		override public function run():void {
			var selectedTestFiles:IList = selection;
			var size:int = selectedTestFiles.length;
			for (var i:int = 0; i < size; i++) {
				CorePlugin.getInstance().serviceLocator.invoke("codeSyncRegexService.generateMatchesForSelection", [Node(selectedTestFiles.getItemAt(i)).nodeUri], null );
			}
		}
	}
}
