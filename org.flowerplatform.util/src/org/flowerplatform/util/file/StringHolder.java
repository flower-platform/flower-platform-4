package org.flowerplatform.util.file;

public class StringHolder extends FileHolder {

	private String content;
	
	private String name;
	
	public StringHolder(String content, String name) {
		this.content = content;
		this.name = name;
	}
	
	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getContent() {
		return content;
	}

}
