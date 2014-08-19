package org.flowerplatform.flex_client.team.git.action.history {
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flex_client.team.git.action.history.ui.GitHistoryView;
	import org.flowerplatform.flexutil.layout.AbstractViewProvider;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 *	@author Vlad Bogdan Manica
	 */ 
	public class GitHistoryViewProvider extends AbstractViewProvider {
		
		public static const ID:String = "githistory";
		
		override public function getId():String {
			return ID;
		}
		
		override public function createView(viewLayoutData:ViewLayoutData):UIComponent {		
			return new GitHistoryView();
		}		
		override public function getTitle(viewLayoutData:ViewLayoutData=null):String	{
			return Resources.getMessage("gitHistory.view");
		}		
		
		override public function getIcon(viewLayoutData:ViewLayoutData=null):Object {
			return Resources.gitHistoryIcon;
		}	
	}
}