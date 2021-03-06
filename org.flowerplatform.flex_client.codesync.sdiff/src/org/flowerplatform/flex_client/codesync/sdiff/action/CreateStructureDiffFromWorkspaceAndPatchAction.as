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
package org.flowerplatform.flex_client.codesync.sdiff.action {
	
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.codesync.sdiff.CreateStructureDiffDialog;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 */ 
	public class CreateStructureDiffFromWorkspaceAndPatchAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.sdiff.action.CreateStructureDiffFromWorkspaceAndPatchAction";
		
		public function CreateStructureDiffFromWorkspaceAndPatchAction() {
			super();
			
			label = Resources.getMessage("codesync.sdiff.action.createSdiffFromWorkspaceAndPatch");
			icon = Resources.gitDiffFromWorkspaceAndPatch;
		}
				
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var dialog:CreateStructureDiffDialog = new CreateStructureDiffDialog();
			dialog.repo = CorePlugin.getInstance().getRepository(node.nodeUri);
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(dialog)
				.setWidth(800)
				.setHeight(500)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
		
	}
}
