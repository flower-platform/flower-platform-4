package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class TempDeleteAfterGH279AndCoAction extends ActionBase
	{
		public function TempDeleteAfterGH279AndCoAction()
		{
			super();
			label = "TempDeleteAfterGH279AndCo";
			visible = true;
		}
		
		override public function run():void
		{
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.tempDeleteAfterGH279AndCo", null); 
		}
		
	}
}