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

package org.flowerplatform.flex_client.team.git.remote {
	
	/**
	 * @author Cristina Brinza
	 */
	
	[RemoteClass(alias="org.flowerplatform.team.git.remote")]
	[Bindable]
	public class GitBranch {
		
		private var _branchName:String;
		private var _branchType:String;
		
		public function GitBranch() {
		}
		
		public function set branchName(value:String):void {
			_branchName = value;
		}
		
		public function set branchType(value:String):void {
			_branchType = value;
		}
		
		public function get branchName():String {
			return _branchName;
		}
		
		public function get branchType():String {
			return _branchType;
		}
	}
}