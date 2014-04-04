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
	import mx.core.Container;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	import mx.messaging.messages.ErrorMessage;
	import mx.rpc.events.FaultEvent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.global_menu.GlobalMenuBar;
	import org.flowerplatform.flexutil.layout.event.ActiveViewChangedEvent;
	import org.flowerplatform.flexutil.layout.event.ViewsRemovedEvent;
	import org.flowerplatform.flexutil.resources.ResourceUpdatedEvent;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	import org.flowerplatform.flexutil.spinner.ModalSpinner;
	
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
			EventDispatcher(FlexUtilGlobals.getInstance().workbench).addEventListener(ActiveViewChangedEvent.ACTIVE_VIEW_CHANGED, CorePlugin.getInstance().resourceNodesManager.activeViewChangedHandler);
			
			CorePlugin.getInstance().perspectives.push(new FlowerPerspective());
					
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
						
			var btn:Button = new Button();
			btn.label = "Open Editor";
			var textInput:TextInput = new TextInput();
			textInput.width = 400;
			textInput.text = "(code|self|D:/temp/repo1/fp-repo-config/FAP-FlowerPlatform4.mm)";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				CorePlugin.getInstance().handleLinkForCommand(CoreConstants.OPEN_RESOURCES, textInput.text);
			});
			hBox.addChild(btn);
			hBox.addChild(textInput);
				
			var addRootBtn:Button = new Button();
			addRootBtn.label = "Open Root";
			addRootBtn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				CorePlugin.getInstance().handleLinkForCommand(CoreConstants.OPEN_RESOURCES, "(root||)");
			});
			hBox.addChild(addRootBtn);
			
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);		

			var menuBar:GlobalMenuBar = new GlobalMenuBar(CorePlugin.getInstance().globalMenuActionProvider);
			menuBar.percentWidth = 100;
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(menuBar, 0);		
						
			CorePlugin.getInstance().getPerspective(FlowerPerspective.ID).resetPerspective(FlexUtilGlobals.getInstance().workbench);
			
			CorePlugin.getInstance().serviceLocator.invoke("coreService.helloServer", [CoreConstants.VERSION], 
				function (result:Object):void {
					// handle any commands to open resources from the URL parameters (e.g. ?openResources=dir/file1,dir/file2)
					CorePlugin.getInstance().handleLink(ExternalInterface.call("getURL"));
					ModalSpinner.removeGlobalModalSpinner();
				},
				function (event:FaultEvent):void {					
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setTitle(Resources.getMessage('version.error'))
					.setText(Resources.getMessage('version.error.message', [CoreConstants.VERSION, ErrorMessage(event.message).rootCause.message]))
					.setWidth(400)
					.setHeight(300)
					.showMessageBox();	
					ModalSpinner.removeGlobalModalSpinner();
				}
			);
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}	
		
		public function invokeSaveResourcesDialog():Boolean {
			CorePlugin.getInstance().resourceNodesManager.showSaveDialogIfDirtyStateOrCloseEditors();
			return CorePlugin.getInstance().resourceNodesManager.getGlobalDirtyState();
		}
		
	}
}
