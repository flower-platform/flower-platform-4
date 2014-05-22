package org.flowerplatform.flex_client.core.editor.action
{
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	public class TestAction extends ActionBase
	{
		public function TestAction()
		{
			super();
			label = "Test Action";
			visible = true;
		}
		
		override public function run():void
		{
			CorePlugin.getInstance().serviceLocator.invoke("nodeService.test", null); 
		}
		
	}
}