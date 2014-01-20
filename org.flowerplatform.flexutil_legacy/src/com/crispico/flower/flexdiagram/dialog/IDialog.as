/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.flexdiagram.dialog {
	
	/**
	 * Dialog that passes the result to an <code>IDialogResultHandler</code> when closed.
	 * The result can be a pressed button, field values or other components depending on the Dialog scope.
	 * 
	 * <p> If the <code>IDialog</code> is closed without a valid result - action aborted, or "Cancel" button is pressed, 
	 * the expected result is <code>null</code>.
	 * 
	 *  
	 * @author Luiza
	 * 
	 */ 
	public interface IDialog {
		
		function setResultHandler(resultHandler:IDialogResultHandler):void;
	}
}