package org.flowerplatform.flex_client.core.editor.action {
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	import spark.formatters.DateTimeFormatter;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ForceUpdateAction extends ActionBase {
		
		public function ForceUpdateAction() {
			super();
			
			parentId = CorePlugin.DEBUG;
			updateLabel();			
		}
		
		public function updateLabel():void {
			if (CorePlugin.getInstance().resourceNodeIdsToNodeUpdateProcessors.lastUpdateTimestampOfClient == -1) {
				label = "No resource updates requested yet";
			} else {
				var formatter:DateTimeFormatter = new DateTimeFormatter();
				formatter.dateTimePattern = "yyyy-MM-dd HH:mm:ss";
				var date:Date = new Date();
				date.time = CorePlugin.getInstance().resourceNodeIdsToNodeUpdateProcessors.lastUpdateTimestampOfClient;
				label = "Last update: " + formatter.format(date);
			}
		}
		
		override public function run():void {
			CorePlugin.getInstance().serviceLocator.invoke("resourceInfoService.ping");
		}
		
	}
}