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

package org.flowerplatform.flex_client.team.git {
	
	/**
	 * @author Cristina Brinza
	 */
	
	public class GitConstants {
		
		public static const GIT_LOCAL_BRANCH_TYPE:String = "gitLocalBranch";
		public static const GIT_REMOTE_BRANCH_TYPE:String = "gitRemoteBranch";
		public static const GIT_TAG_TYPE:String = "gitTag";
		public static const GIT_REPO_TYPE:String = "gitRepo";
		
		public static const GIT_LOCAL_BRANCHES_TYPE:String = "gitLocalBranches";
		public static const GIT_REMOTE_BRANCHES_TYPE:String = "gitRemoteBranches";
		public static const GIT_TAGS_TYPE:String = "gitTags";
			
		/////////////////////////////////////////////////////////////
		//Leaf Git Node Properties
		/////////////////////////////////////////////////////////////
		
		public static const NAME:String = "name";
		public static const FULL_NAME:String = "fullName";
		public static const CONFIG_REMOTE:String = "configRemote";
		public static const CONFIG_UPSTREAM_BRANCH:String = "configUpstreamBranch";
		public static const CONFIG_REBASE:String = "configRebase";	
	}
	
}
