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
package org.flowerplatform.flex_client.mindmap.action {
	
	import org.flowerplatform.flex_client.core.editor.remote.AddChildDescriptor;
	import org.flowerplatform.flex_client.mindmap.MindMapConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class EditNoteInDialogAction extends AbstractEditNodePropertyInDialogAction {
		
		public function EditNoteInDialogAction(descriptor:AddChildDescriptor = null)	{
			super();
				
			label = Resources.getMessage("edit_note_in_dialog_label");
			icon = Resources.mindmap_knotesIcon;
			orderIndex = 90;
		}
		
		override public function run():void {
			editProperty(MindMapConstants.NOTE, Resources.getMessage("note_title"));
		}
		
	}
}
