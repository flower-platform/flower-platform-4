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
		
		private var _updateInterval:int;
		
		public function UpdateTimer(updateInterval:int) {
			_updateInterval = updateInterval;
			
			if (_updateInterval > 0) {
				timer = new Timer(_updateInterval);
				timer.addEventListener(TimerEvent.TIMER, function(event:TimerEvent):void {
					CorePlugin.getInstance().serviceLocator.invoke("resourceService.ping");
				});
				timer.start();
			}
		}
		
		public function get updateInterval():int {
			return _updateInterval;
		}
		
		public function restart(period:int = 0, periodEndedListener:Function = null):void {
			if (updateInterval > 0) {
				timer.reset();
				timer.repeatCount = period / updateInterval;
				if (periodEndedListener != null) {
					timer.addEventListener(TimerEvent.TIMER_COMPLETE, periodEndedListener);
				}
				timer.start();
			}
		}
		
		public function stop():void {
			if (updateInterval > 0) {
				timer.reset();
			}
		}
		
	}
}