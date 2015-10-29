package org.flowerplatform.flexutil.popup.manager {
	import flash.display.DisplayObject;
	
	import mx.core.IFlexDisplayObject;
	
	/**
	 * Note: For the moment it is implemented in XOPS.
	 * @see CustomPopupManager
	 * 
	 * @author Cristina Constantinescu
	 */	
	public interface IPopUpManagerListener {
		
		function onPopUpRemove(popUp:IFlexDisplayObject):void;
		
		function onPopupCenter(popUp:IFlexDisplayObject):Boolean;
		
		function getParentOnPopupAdd(popUp:IFlexDisplayObject, setParent:DisplayObject):DisplayObject;
	}
}