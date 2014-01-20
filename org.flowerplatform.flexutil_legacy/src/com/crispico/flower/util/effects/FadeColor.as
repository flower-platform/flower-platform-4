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
package com.crispico.flower.util.effects
{

	import mx.effects.AnimateProperty;
	import mx.effects.IEffectInstance;	 
	import com.crispico.flower.util.effects.FadeColorInstance;

	/**
	 * Taken from http://ntt.cc/2008/06/22/fade-from-one-color-to-another-animating-color-transitions-in-flex.html.
	 * When changing from a color to another, the color property does not increase incrementaly, 
	 * but for each channel red, green and blue (from color 0x111111, next will be like 0x121212, not 0x111112).
	 * 
	 * 
	 */
	public class FadeColor extends AnimateProperty {
	
		/**
		 * 
		 */
		public function FadeColor(target:Object=null) {
			super(target);
			instanceClass = FadeColorInstance;
		}		
	} 
} 