package org.flowerplatform.flex_client.team.git {
/**
 * @author Diana Balutoiu
 */	
	public class GitConstants {
		
		/////////////////////////////////////////////////////////////
		//Node type in Git folder hierarchy
		/////////////////////////////////////////////////////////////
		
		public static const  GIT_LOCAL_BRANCHES_TYPE:String = "gitLocalBranches";
		public static const GIT_REMOTE_BRANCHES_TYPE:String = "gitRemoteBranches";
		public static const GIT_TAGS_TYPES:String = "gitTags";
		
		public static const  GIT_LOCAL_BRANCH_TYPE:String = "gitLocalBranch";
		public static const GIT_REMOTE_BRANCH_TYPE:String = "gitRemoteBranch";
		public static const GIT_TAG_TYPE:String = "gitTag";
		
		/////////////////////////////////////////////////////////////
		//Leaf Git Node Properties
		/////////////////////////////////////////////////////////////
		
		public static const FULL_NAME:String = "fullName";
		
		
		/////////////////////////////////////////////////////////////
		//Reset types
		/////////////////////////////////////////////////////////////
		
		public static const RESET_SOFT:String = "soft";
		public static const RESET_MIXED:String = "mixed";
		public static const RESET_HARD:String = "hard";
			
	}
}