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
package org.flowerplatform.flex_client.mindmap.action {
	
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class EditNodeDetailsInDialogAction extends AbstractEditNodePropertyInDialogAction {
		
		public static const ID:String = "org.flowerplatform.flex_client.mindmap.action.EditNodeDetailsInDialogAction";		
		
		public function EditNodeDetailsInDialogAction(descriptor:AddChildDescriptor = null)	{
			super();
			label = Resources.getMessage("edit_node_details_in_dialog_label");
			icon = Resources.editDetailsInDialogActionIcon;
			orderIndex = 85;
		}
		
		override public function run():void {
			editProperty(MindMapConstants.NODE_DETAILS, Resources.getMessage("node_details_title"));
		}
					
	}
}
