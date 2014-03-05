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
	
	import mx.containers.HBox;
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import spark.components.Button;
	
	import org.flowerplatform.flex_client.codesync.action.MarkNodeRemovedAction;
	import org.flowerplatform.flex_client.codesync.action.SynchronizeAction;
	import org.flowerplatform.flex_client.codesync.renderer.CodeSyncNodeRenderer;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.mindmap.controller.NodeRendererController;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.mindmap.MindMapPlugin;
	import org.flowerplatform.flexdiagram.controller.renderer.RendererController;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncPlugin;
		
		public static function getInstance():CodeSyncPlugin {
			return INSTANCE;
		}
		
		/**
		 * @author Mariana Gheorghe
		 * @author Cristina Constantinescu
		 */
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CorePlugin.getInstance().serviceLocator.addService("codeSyncOperationsService");
			CorePlugin.getInstance().mindmapEditorClassFactoryActionProvider.addActionClass(MarkNodeRemovedAction);
			CorePlugin.getInstance().mindmapEditorClassFactoryActionProvider.addActionClass(SynchronizeAction);
					
			// controllers for code sync nodes
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(MindMapPlugin.FREEPLANE_PERSISTENCE_CATEGORY)
				.addSingleController(RendererController.TYPE, new NodeRendererController(CodeSyncNodeRenderer));
				
			var hBox:HBox = new HBox();
			hBox.percentWidth = 100;
			var btn:Button = new Button();
			btn.label = "CodeSync";
			btn.addEventListener(MouseEvent.CLICK, function(evt:MouseEvent):void {
				CorePlugin.getInstance().serviceLocator.invoke("codeSyncOperationsService.synchronize", [null]);
			});
			hBox.addChild(btn);
			IVisualElementContainer(FlexGlobals.topLevelApplication).addElementAt(hBox, 0);		
		}
		
	}
}
