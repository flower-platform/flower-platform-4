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
package org.flowerplatform.flex_client.core.editor.action {
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.core.editor.ui.RichTextWithRendererView;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.controller.TypeDescriptorRegistry;
	import org.flowerplatform.flexutil.controller.ValuesProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RenameAction extends DiagramShellAwareActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.core.editor.action.RenameAction";
		
		public function RenameAction() {			
			label = Resources.getMessage("mindmap.edit.node.core");
			icon = Resources.editIcon;
			orderIndex = 80;
		}
								
		override public function run():void {
			var node:Node = Node(selection.getItemAt(0));
			
			var view:RichTextWithRendererView = new RichTextWithRendererView();	
			view.node = node;
			view.diagramShellContext = diagramShellContext;
			
			var registry:TypeDescriptorRegistry = diagramShell.getRegistryForModel(node);
			var valuesProvider:ValuesProvider = CorePlugin.getInstance().getNodeValuesProviderForMindMap(registry, node);
			view.text = String(valuesProvider.getValue(registry, node, FlexDiagramConstants.BASE_RENDERER_TEXT));
			
			view.resultHandler = function(newValue:String):void {				
				// invoke service method and wait for result to close the rename popup
				CorePlugin.getInstance().serviceLocator.invoke("nodeService.setProperty", [node.nodeUri, valuesProvider.getPropertyName(registry, node, FlexDiagramConstants.BASE_RENDERER_TEXT), newValue], 
					function(data:Object):void {
						if (view != null) {
							FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(view);
							view = null;
						};
					});
			};
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
				.setViewContent(view)
				.setWidth(500)
				.setHeight(400)
				.setTitle(label)
				.setIcon(icon)
				.show();
		}
	}
}
