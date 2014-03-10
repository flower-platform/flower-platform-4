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
package org.flowerplatform.flex_client.core.editor.update {
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import org.flowerplatform.flex_client.core.CorePlugin;
	
	/**
	 * Pings the server periodically. It is reset after a server method is invoked.
	 * 
	 * @author Mariana Gheorghe
	 */
	public class UpdateTimer {
		
		private var timer:Timer;
		
		private var active:Boolean = true;
		
		private var updateInterval:int = 5000;
		
		public function UpdateTimer() {
			if (active) {
				timer = new Timer(updateInterval);
				timer.addEventListener(TimerEvent.TIMER, function(event:TimerEvent):void {
					CorePlugin.getInstance().serviceLocator.invoke("resourceInfoService.ping");
				});
				timer.start();
			}
		}
		
		public function restart():void {
			if (active) {
				timer.reset();
				timer.start();
			}
		}
		
	}
}