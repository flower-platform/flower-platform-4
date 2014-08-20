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
package org.flowerplatform.flex_client.codesync {
	
	
	import org.flowerplatform.flex_client.codesync.action.SynchronizeAction;
	import org.flowerplatform.flex_client.codesync.node.renderer.CodeSyncNodeRenderer;
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.editor.action.ActionDescriptor;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flex_client.mindmap.controller.NodeRendererController;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.flexutil.controller.AbstractController;
	
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
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(CodeSyncConstants.CATEGORY_CODESYNC)
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER, AbstractController(new NodeRendererController(CodeSyncNodeRenderer, -10000)));
			
			CorePlugin.getInstance().serviceLocator.addService("codeSyncOperationsService");
//			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(SynchronizeAction);
//			CorePlugin.getInstance().actionRegistry[SynchronizeAction.actionId] = new FactoryWithInitialization(SynchronizeAction).newInstance();
			
			FlexUtilGlobals.getInstance().registerAction(SynchronizeAction);
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateCategoryTypeDescriptor(CodeSyncConstants.CATEGORY_CODESYNC)
				.addAdditiveController(CoreConstants.ACTION_DESCRIPTOR, new ActionDescriptor(SynchronizeAction.ID));
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
	}
}