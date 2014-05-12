/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
package org.flowerplatform.flex_client.core.shortcut {
	import mx.core.FlexGlobals;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flex_client.core.CoreConstants;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.popup.IMessageBox;
	import org.flowerplatform.flexutil.shortcut.AssignShortcutForActionEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AssignHotKeyAction extends ActionBase {
		
		public function AssignHotKeyAction() {
			super();
			label = Resources.getMessage("asign.hotkey.action.label");
			icon = Resources.keyboardIcon;
			parentId = CoreConstants.TOOLS_MENU_ID;
		}
		
		override public function run():void	{
			FlexUtilGlobals.getInstance().keyBindings.learnShortcutOnNextActionInvocation = true;
			
			var messageBox:IMessageBox = FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()		
				.setTitle(label)
				.setText(Resources.getMessage("shortcut.action.click"))
				.setWidth(300)
				.setHeight(100)
				.addButton(FlexUtilAssets.INSTANCE.getMessage('dialog.cancel'), function():void {FlexUtilGlobals.getInstance().keyBindings.learnShortcutOnNextActionInvocation = false;});
			
			messageBox.showMessageBox(false);
			
			var afterSelectingAnActionHandler:Function = function(event:AssignShortcutForActionEvent):void {
				// remove message box and listener
				FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(UIComponent(messageBox));				
				UIComponent(FlexGlobals.topLevelApplication).stage.removeEventListener(AssignShortcutForActionEvent.ASSIGN_SHORTCUT_FOR_ACTION, afterSelectingAnActionHandler);
				
				// show view to enter shortcut for selected actionId
				var view:AssignShorcutForActionView = new AssignShorcutForActionView();
				view.actionId = event.actionId;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()				
					.setViewContent(view)
					.setWidth(450)
					.setHeight(130)
					.show();
			};
			UIComponent(FlexGlobals.topLevelApplication).stage.addEventListener(AssignShortcutForActionEvent.ASSIGN_SHORTCUT_FOR_ACTION, afterSelectingAnActionHandler);
		}
	
	}
}