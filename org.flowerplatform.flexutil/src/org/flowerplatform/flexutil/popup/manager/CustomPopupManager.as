package org.flowerplatform.flexutil.popup.manager {
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.core.IFlexDisplayObject;
	import mx.core.IFlexModuleFactory;
	import mx.managers.PopUpManagerImpl;
	
	/**
	 * Created to make additional operations when a popup is added/removed/centered.
	 * @see CustomPreloader
	 * 
	 * @author Cristina Constantinescu
	 */	
	public class CustomPopupManager extends PopUpManagerImpl {
		
		private static var instance:CustomPopupManager;
		
		private var listeners:ArrayCollection;
		
		static public function getInstance():CustomPopupManager {
			if (!instance) {
				instance = new CustomPopupManager();
			}
			return instance;
		};
		
		public function addListener(listener:IPopUpManagerListener):void {
			if (listeners == null) {
				listeners = new ArrayCollection();
			}
			listeners.addItem(listener);
		}
		
		public function removeListener(listener:IPopUpManagerListener):void {
			if (listeners != null) {
				listeners.removeItem(listener);
			}
		}
		
		override public function removePopUp(popUp:IFlexDisplayObject):void {		
			if (listeners != null) {
				for (var i:int = 0; i < listeners.length; i++) {
					IPopUpManagerListener(listeners.getItemAt(i)).onPopUpRemove(popUp);
				}
			}						
			super.removePopUp(popUp);
		}
		
		override public function centerPopUp(popUp:IFlexDisplayObject):void {	
			var preventDefault:Boolean = false;
			if (listeners != null) {
				for (var i:int = 0; i < listeners.length; i++) {
					var pd:Boolean = IPopUpManagerListener(listeners.getItemAt(i)).onPopupCenter(popUp);
					if (pd) {
						preventDefault = pd;
					}
				}
			}
			if (!preventDefault) {
				super.centerPopUp(popUp);
			}			
		}
		
		override public function addPopUp(popup:IFlexDisplayObject, setParent:DisplayObject, modal:Boolean = false, childList:String = null, moduleFactory:IFlexModuleFactory = null):void {
			var parent:DisplayObject = setParent;
			if (listeners != null) {
				for (var i:int = 0; i < listeners.length; i++) {
					parent = IPopUpManagerListener(listeners.getItemAt(i)).getParentOnPopupAdd(setParent);
				}
			}
			super.addPopUp(popup, parent, modal, childList, moduleFactory);
		}
		
	}
}