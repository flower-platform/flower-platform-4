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
package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	public class ModalSpinnerViewHost extends ModalSpinner implements IViewHost {
		
		public function ModalSpinnerViewHost(viewContent:IViewContent) {
			super();
			dontShowSpinner = true;
			childrenUnderSpinner = [viewContent];
		}
		
		public function get activeViewContent():IViewContent {
			return IViewContent(childrenUnderSpinner[0]);
		}
		
		public function setActiveViewContent(value:IViewContent, viaFocusIn:Boolean = false):void {
		
		}
		
		public function selectionChanged():IList {
			// doesn't support this			
			return null;
		}
		
		public function setIcon(value:Object):void {
			// doesn't support this			
		}
		
		public function setLabel(value:String):void {
			// doesn't support this			
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
			// doesn't support this	
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
			// doesn't support this	
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function openMenu(x:Number, y:Number, context:Object, parentActionId:String = null):Boolean {
			// doesn't support this	
			return false;
		}	
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {	
			// doesn't support this	
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {
			// doesn't support this	
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function getCachedActions():Vector.<IAction> {	
			// doesn't support this	
			return null;
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function getCachedSelection():IList {
			// doesn't support this	
			return null;
		}	
	}
}