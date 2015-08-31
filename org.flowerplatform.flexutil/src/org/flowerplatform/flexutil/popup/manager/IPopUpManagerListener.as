package org.flowerplatform.flexutil.popup.manager {
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
		
	}
}