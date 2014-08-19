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
package {

	// Template comment
			/* comm */
			/** doc comm */
			// after line

	// TODO astea de facut in testFile separate
	/*
	public class Class A {
	*/  
	public class
			/* 
			 comm */
			/** 
			 * doc comm */
			// after line
		ClassA 
			/* comm */
			/** doc comm */
			// after line
		extends 
			/* comm */
			/** doc comm */
			// after line
		com.ClassB 
			/* comm */
			/** doc comm */
			// after line
		implements 
			/* comm */
			/** doc comm */
			// after line
		Interface1
			/* comm */
			/** doc comm */
			// after line
		, 
			/* comm */
			/** doc comm */
			// after line
		Interface2
			/* comm */
			/** doc comm */
			// after line
		{
		
		var attr1:String;
		
		const attr2:String;
		
		private static var attr3; 
		
			/* comm */
			/** doc comm */
			// after line
		var
			/* comm */
			/** doc comm */
			// after line
		attr4
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		:String;
		
		var _attr5:String;
		
		var $attr6:String;
		
		public function method1 ():void {
			
		}
		
		function get method2 (par:String)
			
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		private
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		function
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		set
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		method3
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		(
			/* 
			comm */
			/** 
			 * doc comm */
			// after line
		par:String) {
			
		}
		
		function 
			//
		set
			//
		method4
			//
		() {
		}
			
			
		function
			//
		method5
			//
		() {
		}
		
			
		\bfunction\b  [\s/]*   (?:set|get)?   [\s/]*  (\w+)
				
				Sa incerc maine sa folosesc fix expresia pentru methoda pe care o construieste in java
		
		\bfunction\b  
		(?:
			\s | 
			/\*.*?\*/ |
			//[^\r^\n]*?\r?\n
		)*?
		(?:
			\bget\b |
			\bset\b
		)?
		(?:
			\s | 
			/\*.*?\*/ |
			//[^\r^\n]*?\r?\n
		)*?
		([a-zA-Z_\$][\w_\$]*)
		(?:
			\s | 
			/\*.*?\*/ |
			//[^\r^\n]*?\r?\n
		)*?
		\(
		
			(?: \s   |   /\*.*?\*/   |//[^\r^\n]*?\r?\n   )   *? comentarii sau spatii cat mai multe!!!
				
			(?: \s   |   /\*.*?\*/   |//[^\r^\n]*?\r?\n)*?
				
			inchiderea comentariului trebuie sa se faca cat mai devreme, adica *? lazy
			insa comentariile trebuie sa fie cat mai multe luate in seama adica * greedy
				
			\bfunction\b(?:\s|/\*.*?\*/|//[^\r^\n]*?\r?\n)*?(?:\bget\b|\bset\b)?(?:\s|/\*.*?\*/|//[^\r^\n]*?\r?\n)*?([a-zA-Z_\$][\w_\$]*)(?:\s|/\*.*?\*/|//[^\r^\n]*?\r?\n)*?\(
			
			\bfunction\b(?:\s|/\*.*?\*/|//[^\r^\n]*\r?\n?)*?(?:\bget\b|\bset\b)?(?:\s|/\*.*?\*/|//[^\r^\n]*\r?\n?)*?([a-zA-Z_\$][\w_\$]*)(?:\s|/\*.*?\*/|//[^\r^\n]*\r?\n?)*?\(	
				
			\bfunction\b
				(?:\s|/\*.*?\*/|//[^\r^\n]*\r?\n?)*?
			(?:\bget\b|\bset\b)?
				(?:\s|/\*.*?\*/|//[^\r^\n]*\r?\n?)*?
			([a-zA-Z_\$][\w_\$]*)
				(?:\s|/\*.*?\*/|//[^\r^\n]*\r?\n?)*?
			\(				
				
	}
}