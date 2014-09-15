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
package org.flowerplatform.flex_client.team.git.action {
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.ui.ConfigureBranchView;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;

	/**
 	* @author Diana Balutoiu 
 	*/
	public class ConfigureBranchAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.team.git.action.ConfigureBranchAction";
		
		public function ConfigureBranchAction() {
			super();
			label = Resources.getMessage('flex_client.team.git.action.configureBranch');
			icon = Resources.configBranchIcon;
			orderIndex = 320;
		}
				
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var configBranchView:ConfigureBranchView = new ConfigureBranchView();			
			configBranchView.node = node;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(configBranchView)
				.setWidth(500)
				.setHeight(200)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}
