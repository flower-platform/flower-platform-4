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
package org.flowerplatform.flex_client.host_app.mobile.view_server_account
{
	import org.flowerplatform.flex_client.host_app.mobile.view_edit_server_account.EditServerAccountView;
	import org.flowerplatform.flex_client.resources.Resources;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Sebastian Solomon
	 */
	public class AddServerAccountAction extends ActionBase {
		public var serverAccountView:ServerAccountsView;
		
		public function AddServerAccountAction() {
			super();
			label = Resources.getMessage("mobile.add.server.account.label");
		}
		
		override public function run():void {
			var editAccountView:EditServerAccountView = new EditServerAccountView();
			editAccountView.setServerAccountView(serverAccountView);
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setTitle(Resources.getMessage("mobile.edit.server.account.title"))
				.setViewContent(editAccountView)
				.show();			
		}
	}
}