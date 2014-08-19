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
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.remote.Node;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.text.TextEditorFrontend;
	import org.flowerplatform.flexdiagram.mindmap.MindMapRootModelWrapper;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.SelectObjListPopup;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GenerateMatchesAction extends ActionBase {
				
		public function GenerateMatchesAction() {
			super();
			preferShowOnActionBar = true;
			label = Resources.getMessage("regex.generate");
			icon = Resources.reloadIcon;
		}
		
		override public function get visible():Boolean {			
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			return obj is Node && 
				(Node(obj).type == CodeSyncRegexConstants.REGEX_CONFIG_TYPE 
					|| Node(obj).type == CodeSyncRegexConstants.REGEX_MACRO_TYPE 
					|| Node(obj).type == CodeSyncRegexConstants.REGEX_TYPE);
		}
		
		override public function run():void {
			var obj:Object = selection.getItemAt(0);
			if (obj is MindMapRootModelWrapper) {
				obj = MindMapRootModelWrapper(obj).model;
			}
			
			// get all text editors
			var resourceUris:Array = new Array();			
			var components:ArrayCollection = new ArrayCollection();
			FlexUtilGlobals.getInstance().workbench.getAllEditorViews(null, components);
			
			for each (var component:UIComponent in components) {								
				if (component is TextEditorFrontend) {
					resourceUris.push(CorePlugin.getInstance().nodeRegistryManager.getResourceUrisForNodeRegistry(TextEditorFrontend(component).nodeRegistry)[0]);
				}
			}
			
			if (resourceUris.length == 0) {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(Resources.getMessage("regex.generateMatches.noFileOpen"))
					.setHeight(100)
					.setWidth(400)
					.setTitle(Resources.getMessage("info"))
					.setIcon(FlexUtilAssets.iconInfo)
					.showMessageBox();
				return;
			}
			
			var view:SelectObjListPopup = new SelectObjListPopup();
			view.listProvider = new ArrayList(resourceUris);
			view.resultHandler = function(resourceUri:String):void {
				CorePlugin.getInstance().serviceLocator.invoke("codeSyncRegexService.generateMatches", 
					[Node(obj).nodeUri, resourceUri], 
					function(matchUri:String):void {CorePlugin.getInstance().openEditor(new Node(matchUri), null, true)});
			};
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("regex.generateMatches.title"))
				.setIcon(Resources.reloadIcon)
				.setViewContent(view)
				.setHeight(300)
				.setWidth(300)
				.show();			
		}
		
	}
}