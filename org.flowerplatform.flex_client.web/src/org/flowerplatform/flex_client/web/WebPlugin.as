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
package org.flowerplatform.flex_client.web {
	import flash.events.EventDispatcher;
	import flash.external.ExternalInterface;
	
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.global_menu.GlobalMenuBar;
	import org.flowerplatform.flexutil.iframe.FlowerIFrameViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	import org.flowerplatform.flexutil.layout.event.ActiveViewChangedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class WebPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebPlugin;
		
		public static function getInstance():WebPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;	
		
			if (ExternalInterface.available) { 
				ExternalInterface.addCallback("invokeSaveResourcesDialog", invokeSaveResourcesDialog); 
			}			
		}
		
		override public function start():void {
			super.start();
					
			EventDispatcher(FlexUtilGlobals.getInstance().workbench).addEventListener(ViewsRemovedEvent.VIEWS_REMOVED, CorePlugin.getInstance().resourceNodesManager.viewsRemovedHandler);
			EventDispatcher(FlexUtilGlobals.getInstance().workbench).addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, CorePlugin.getInstance().resourceNodesManager.activeViewChangedHandler);
			
			CorePlugin.getInstance().perspectives.push(new FlowerPerspective());
					
			var menuBar:GlobalMenuBar = new GlobalMenuBar(CorePlugin.getInstance().globalMenuActionProvider);
			menuBar.percentWidth = 100;
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(menuBar, 0);		
						
			CorePlugin.getInstance().getPerspective(FlowerPerspective.ID).resetPerspective(FlexUtilGlobals.getInstance().workbench);
			
			CorePlugin.getInstance().handleLinkForCommand(CoreConstants.OPEN_RESOURCES, "virtual:user/repo|root");
			CorePlugin.getInstance().handleLink(ExternalInterface.call("getURL"));
			
			// test for embedded IFrame
			var viewLayoutData:ViewLayoutData = new ViewLayoutData(FlowerIFrameViewProvider.ID, "js_client.core/index.html");
			viewLayoutData.isEditor = true;
			FlexUtilGlobals.getInstance().workbench.addEditorView(viewLayoutData, true);
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}	
		
		public function invokeSaveResourcesDialog():Boolean {
			CorePlugin.getInstance().nodeRegistryManager.resourceOperationsManager.showSaveDialog();
			return CorePlugin.getInstance().resourceNodesManager.getGlobalDirtyState();
		}
		
	}
}
