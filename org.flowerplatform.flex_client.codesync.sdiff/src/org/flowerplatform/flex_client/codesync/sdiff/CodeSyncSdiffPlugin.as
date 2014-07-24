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
package org.flowerplatform.flex_client.codesync.sdiff {
	import org.flowerplatform.flex_client.codesync.CodeSyncConstants;
	import org.flowerplatform.flex_client.codesync.sdiff.action.CodeSyncNewConstants;
	import org.flowerplatform.flex_client.codesync.sdiff.action.CreateStructureDiffFromWorkspaceAndPatchAction;
	import org.flowerplatform.flex_client.codesync.sdiff.controller.PathNodeController;
	import org.flowerplatform.flex_client.codesync.sdiff.renderer.PathNodeRenderer;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexdiagram.FlexDiagramConstants;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class CodeSyncSdiffPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncSdiffPlugin;
		
		public static function getInstance():CodeSyncSdiffPlugin {
			return INSTANCE;
		}
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			CorePlugin.getInstance().serviceLocator.addService("structureDiffService");
			
			CorePlugin.getInstance().editorClassFactoryActionProvider.addActionClass(CreateStructureDiffFromWorkspaceAndPatchAction);
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncNewConstants.MATCH)
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER,new PathNodeController(PathNodeRenderer));
			
			CorePlugin.getInstance().nodeTypeDescriptorRegistry.getOrCreateTypeDescriptor(CodeSyncNewConstants.LEGEND)
				.addSingleController(FlexDiagramConstants.RENDERER_CONTROLLER,new PathNodeController(PathNodeRenderer));
			
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
	}
}