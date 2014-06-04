package org.flowerplatform.core.file;

/**
 * @author Mariana Gheorghe
 */
public class FileExtensionSetting {

	private String scheme;
	
	private String contentType;

	public FileExtensionSetting(String scheme, String contentType) {
		super();
		this.scheme = scheme;
		this.contentType = contentType;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
