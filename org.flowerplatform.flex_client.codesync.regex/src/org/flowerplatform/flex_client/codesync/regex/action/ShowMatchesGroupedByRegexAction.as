package org.flowerplatform.flex_client.codesync.regex.action {
	import org.flowerplatform.flex_client.codesync.regex.CodeSyncRegexConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowMatchesGroupedByRegexAction extends ShowOrderedMatchesAction {
		
		public static const ID:String = "org.flowerplatform.flex_client.codesync.regex.action.ShowMatchesGroupedByRegexAction";
		
		public function ShowMatchesGroupedByRegexAction() {			
			label = Resources.getMessage("regex.action.grouped");
			icon = Resources.bricksIcon;
		}
					
		override protected function getExpandContext():Object {
			var context:Object = super.getExpandContext();
			context[CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX] = true;
			return context;
		}
		
	}
}