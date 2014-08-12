package org.flowerplatform.team.git.remote;

public class GitCredentials {
	
	private String username;
	
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public GitCredentials() {
		
	}
	
	public GitCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
