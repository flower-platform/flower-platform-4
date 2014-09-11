package org.flowerplatform.team.git.remote;

/**
 * @author Andreea Tita
 */
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
	
	/**
	 *@author see class
	 **/
	public GitCredentials() {
		super();
	}
	
	/**
	 *@author see class
	 **/
	public GitCredentials(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
