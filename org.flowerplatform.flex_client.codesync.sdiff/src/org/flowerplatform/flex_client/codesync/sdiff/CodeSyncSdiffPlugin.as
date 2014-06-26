package org.flowerplatform.flex_client.codesync.sdiff {
	import org.flowerplatform.flex_client.codesync.sdiff.action.CreateStructureDiffFromWorkspaceAndPatchAction;
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flex_client.core.plugin.AbstractFlowerFlexPlugin;
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
		}
		
		override protected function registerMessageBundle():void {
			// messages come from .flex_client.resources
		}
		
	}
}