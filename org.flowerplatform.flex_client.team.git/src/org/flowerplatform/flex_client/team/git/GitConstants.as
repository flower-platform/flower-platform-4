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

	import org.flowerplatform.flexutil.FlexUtilConstants;
	
	public class GitConstants {

		public static const GIT_CATEGORY:String = FlexUtilConstants.CATEGORY_PREFIX + "git";
		public static const GIT_REF_CATEGORY:String = FlexUtilConstants.CATEGORY_PREFIX + "gitRef";
				
		/////////////////////////////////////////////////////////////
		//Node type in Git folder hierarchy
		/////////////////////////////////////////////////////////////

		public static const GIT_REPO_TYPE:String = "gitRepo";
		public static const GIT_LOCAL_BRANCHES_TYPE:String = "gitLocalBranches";
		public static const GIT_REMOTE_BRANCHES_TYPE:String = "gitRemoteBranches";
		public static const GIT_TAGS_TYPE:String = "gitTags";
		public static const GIT_REMOTES_TYPE:String = "gitRemotes";
		public static const GIT_LOCAL_BRANCH_TYPE:String = "gitLocalBranch";
		public static const GIT_REMOTE_BRANCH_TYPE:String = "gitRemoteBranch";
		public static const GIT_TAG_TYPE:String = "gitTag";
		public static const GIT_REMOTE_TYPE:String = "gitRemote";

		/////////////////////////////////////////////////////////////
		//Schemes
		/////////////////////////////////////////////////////////////
		
		public static const GIT_SCHEME:String = "git";
		
		/////////////////////////////////////////////////////////////
		// Git Node Properties
		/////////////////////////////////////////////////////////////

		public static const NAME:String = "name";
		public static const COMMIT_ID:String = "commitID";

		public static const IS_GIT_REPOSITORY:String = "isGitRepository";

		public static const FULL_NAME:String = "fullName";
		
		public static const CONFIG_REMOTE:String = "configRemote";

		public static const CONFIG_UPSTREAM_BRANCH:String = "configUpstreamBranch";
		public static const CONFIG_REBASE:String = "configRebase";

		public static const CURRENT_BRANCH:String = "currentBranch";		
		
		public static const IS_CHECKEDOUT:String = "isCheckedOut";
		
		public static const FETCH_REF_SPECS:String = "fetchRefSpecs";
		public static const PUSH_REF_SPECS:String = "pushRefSpecs";
		public static const REMOTE_URIS:String = "URIs";

		/////////////////////////////////////////////////////////////
		//Reset types
		/////////////////////////////////////////////////////////////
		
		public static const RESET_SOFT:String = "soft";
		public static const RESET_MIXED:String = "mixed";
		public static const RESET_HARD:String = "hard";	
	
		/////////////////////////////////////////////////////////////
		//Git Staging Types
		/////////////////////////////////////////////////////////////
		
		public static const FILE_ADDED:String = "ADD";
		public static const FILE_MODIFIED:String = "MODIFY";
		public static const FILE_DELETED:String = "DELETE";
		public static const FILE_RENAMED:String = "RENAME";
		public static const FILE_COPIED:String = "COPY";
	}
}
