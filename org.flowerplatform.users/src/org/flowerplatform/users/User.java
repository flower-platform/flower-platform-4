package org.flowerplatform.users;

public class User {

	private String login;
	
	private String name;
	
	private TestObject test = new TestObject();
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public TestObject getTest() {
		return test;
	}

	public void setTest(TestObject test) {
		this.test = test;
	}

}
