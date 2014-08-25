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
package org.flowerplatform.flex_client.core.editor.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.BasicEditorDescriptor;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.Pair;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 * @author Cristina Brinza
	 */
	public class OpenAction extends ActionBase {
		
		public var contentType:String;
		
		public var resourceUri:String;
		
		public function OpenAction(resourceUri:String = null, contentType:String = null) {
			super();
						
			this.contentType = contentType;
			this.resourceUri = resourceUri;
			
			if (contentType != null) {
				var editorDescriptor:BasicEditorDescriptor = CorePlugin.getInstance().contentTypeRegistry[contentType];
				label = editorDescriptor.getEditorName();
				icon = editorDescriptor.getIcon();
				parentId = OpenWithEditorComposedAction.ACTION_ID_OPEN_WITH;
			} else {
				orderIndex = 20;
				label = Resources.getMessage("editor.action.open");
				icon = Resources.openIcon;
			}
		}
		
		override public function get visible():Boolean {			
			return selection != null && selection.length == 1 && selection.getItemAt(0) is Node;
		}
		
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			var subscribableResources:ArrayCollection = ArrayCollection(node.getPropertyValue(CoreConstants.SUBSCRIBABLE_RESOURCES));
			
			if (resourceUri == null && subscribableResources != null && subscribableResources.length > 0) {
				resourceUri = Pair(subscribableResources.getItemAt(0)).a as String;
			} else if (resourceUri == null) {
				resourceUri = node.nodeUri;
			}
			
			CorePlugin.getInstance().openEditor(resourceUri, contentType);
		}
		
	}
}