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
package org.flowerplatform.flex_client.web {
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.external.ExternalInterface;
	
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.SaveResourceNodesView;
	import org.flowerplatform.flex_client.core.event.GlobalActionProviderChangedEvent;
	import org.flowerplatform.flex_client.core.link.LinkHandler;
	import org.flowerplatform.flex_client.core.mindmap.layout.MindMapPerspective;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.global_menu.GlobalMenuBar;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	
	import spark.components.Application;
	import spark.components.Button;
	import spark.components.TextInput;
	
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
						
			CorePlugin.getInstance().getPerspective(MindMapPerspective.ID).resetPerspective(FlexUtilGlobals.getInstance().workbench);
			
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
						
			var btn:Button = new Button();
			btn.label = "Open Editor";
			var textInput:TextInput = new TextInput();
			textInput.width = 400;
			textInput.text = "resource||D:/temp/FAP-FlowerPlatform4.mm";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				CorePlugin.getInstance().handleLinkForCommand(LinkHandler.OPEN_RESOURCES, textInput.text);
			});
			hBox.addChild(btn);
			hBox.addChild(textInput);
				
			var addRootBtn:Button = new Button();
			addRootBtn.label = "Add root";
			addRootBtn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				new SaveResourceNodesView().show(null, null);
//				CorePlugin.getInstance().handleLinkForCommand(LinkHandler.OPEN_ROOT, null);
			});
			hBox.addChild(addRootBtn);
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);		
						
			var menuBar:GlobalMenuBar = new GlobalMenuBar(CorePlugin.getInstance().globalMenuActionProvider);
			menuBar.percentWidth = 100;
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(menuBar, 0);		
			
			Application(FlexGlobals.topLevelApplication).addEventListener(GlobalActionProviderChangedEvent.ACTION_PROVIDER_CHANGED,
				function (event:GlobalActionProviderChangedEvent):void {
					//update menu provider with new content
					menuBar.actionProvider = CorePlugin.getInstance().globalMenuActionProvider;
				}
			);			
									
			CorePlugin.getInstance().handleLink(CorePlugin.getInstance().getAppUrl());		
		}
		
		override protected function registerMessageBundle():void {			
		}	
			
		public function invokeSaveResourcesDialog():Boolean {
			CorePlugin.getInstance().resourceNodesManager.invokeSaveResourceNodesView();
			return CorePlugin.getInstance().resourceNodesManager.getGlobalDirtyState();
		}
		
	}
}
