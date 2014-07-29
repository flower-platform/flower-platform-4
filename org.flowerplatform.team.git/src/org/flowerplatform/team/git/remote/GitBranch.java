package org.flowerplatform.team.git.remote;

/**
 *
 * @author Cristina Brinza
 *
 */

public class GitBranch {

	private String branchName;
	private String branchType;
	
	public GitBranch() {
		super();
	}
	
	public GitBranch(String branchName, String branchType) {
		super();
		this.branchName = branchName;
		this.branchType = branchType;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchType() {
		return branchType;
	}

	public void setBranchType(String branchType) {
		this.branchType = branchType;
	}
	
	
	
}
