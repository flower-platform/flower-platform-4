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
package org.flowerplatform.flex_client.codesync {
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	import mx.rpc.events.ResultEvent;
	
	import org.flowerplatform.flex_client.codesync.action.MarkNodeRemovedAction;
	import org.flowerplatform.flex_client.codesync.remote.CodeSyncOperationsService;
	import org.flowerplatform.flex_client.codesync.renderer.CodeSyncNodeRenderer;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.properties.PropertiesPlugin;
	import org.flowerplatform.flex_client.properties.property_renderer.DropDownListPropertyRenderer;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.Utils;
	
	import spark.components.Button;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncPlugin;
		
		public static function getInstance():CodeSyncPlugin {
			return INSTANCE;
		}
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CorePlugin.getInstance().mindmapNodeRendererControllerClass = CodeSyncNodeRenderer;
			
			CorePlugin.getInstance().serviceLocator.addService(CodeSyncOperationsService.ID);
			CorePlugin.getInstance().serviceLocator.addService("gitHubOperationsService");
			CorePlugin.getInstance().mindmapEditorClassFactoryActionProvider.addActionClass(MarkNodeRemovedAction);
		
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
			var btn:Button = new Button();
			btn.label = "CodeSync";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				new CodeSyncOperationsService().synchronize(null);
			});
			hBox.addChild(btn);
			
			btn = new Button();
			btn.label = "GitHub";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				CorePlugin.getInstance().serviceLocator.invoke("gitHubOperationsService.synchronize");
			});
			hBox.addChild(btn);
			
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);		
			
			CorePlugin.getInstance().serviceLocator.invoke(CodeSyncOperationsService.ID + ".getDropdownPropertyRenderersInfo", null, function(result:Object):void {
				var names:ArrayCollection = result["names"];
				var dataProviders:Object = result["dataProviders"];
				for each (var name:String in names) {
					PropertiesPlugin.getInstance().propertyRendererClasses[name] = new FactoryWithInitialization(DropDownListPropertyRenderer, 
						{
							requestDataProviderHandler: function (callbackObject:Object, callbackFunction:Function):void {
								callbackFunction.call(callbackObject, dataProviders[name]);
							},
							
							labelFunction: function (object:Object):String {
								return object.toString();
							},
							
							getItemIndexFromList: function (item:Object, list:ArrayCollection):int {
								if (item != null) {
									for (var i:int = 0; i < list.length; i++) {
										var listItem:Object = list.getItemAt(i);
										if (item == listItem) {
											return i;
										}
									}
								}
								return -1;
							}
						});
				}
			});
				
		}
		
	}
}
