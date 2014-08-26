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
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.codesync.regex.ui.GenerateMatchFileView;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.EditorFrontend;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.text.TextEditorFrontend;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GenerateMatchesAction extends ActionBase {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.GenerateMatchesAction";
				
		public function GenerateMatchesAction() {
			super();
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.generate");
			icon = Resources.reloadIcon;
		}
		
		override public function run():void {
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			
			// get all text editors
			var textResourceUris:Array = new Array();	
			var allResourceUris:Array = new Array();	
			var components:ArrayCollection = new ArrayCollection();
			FlexUtilGlobals.getInstance().workbench.getAllEditorViews(null, components);
			
			for each (var component:UIComponent in components) {								
				if (component is EditorFrontend) {
					var resourceUri:String = CorePlugin.getInstance().nodeRegistryManager.getResourceUrisForNodeRegistry(EditorFrontend(component).nodeRegistry)[0]; 
					if (component is TextEditorFrontend) {
						textResourceUris.push(resourceUri);		
					}
					allResourceUris.push(resourceUri);
				}				
			}
			
			if (textResourceUris.length == 0) {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("regex.generateMatches.noFileOpen"))
					.setHeight(100)
					.setWidth(400)
					.setTitle(Resources.getMessage("info"))
					.setIcon(FlexUtilAssets.iconInfo)
					.showMessageBox();
				return;
			}
			
			var view:GenerateMatchFileView = new GenerateMatchFileView();
			view.listProvider = new ArrayList(textResourceUris);
			view.node = Node(obj);
			view.resultHandler = function(data:Array):void {
				CorePlugin.getInstance().serviceLocator.invoke("codeSyncRegexService.generateMatches", 
					[Node(obj).nodeUri, data[0], data[1], data[2]], 
					function(matchUri:String):void { 
						if (allResourceUris.indexOf(matchUri) != -1) {
							CorePlugin.getInstance().nodeRegistryManager.serviceInvocator.invoke("resourceService.reload", [CorePlugin.getInstance().nodeRegistryManager.getResourceSetsForResourceUris([matchUri])[0]]);
						} else {
							CorePlugin.getInstance().openEditor(new Node(matchUri), null);
						}
					});
			};
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("regex.generateMatches.title"))
				.setIcon(Resources.reloadIcon)
				.setViewContent(view)
				.setHeight(300)
				.setWidth(600)
				.show();			
		}
		
	}
}
