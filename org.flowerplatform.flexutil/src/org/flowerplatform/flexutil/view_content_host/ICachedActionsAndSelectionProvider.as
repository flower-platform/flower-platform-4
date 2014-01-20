package org.flowerplatform.flexutil.view_content_host {
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.action.IAction;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public interface ICachedActionsAndSelectionProvider	{
		
		function getCachedSelection():IList;
		
		function getCachedActions():Vector.<IAction>;
		
	}
}