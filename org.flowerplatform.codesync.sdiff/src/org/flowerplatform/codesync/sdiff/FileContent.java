package org.flowerplatform.codesync.sdiff;

/**
 * A FileContent object encapsulates the content of a file from an old git
 * revision as well as the content of the same file from a new git revision.
 * 
 * @author Valentina-Camelia Bojan
 */
public class FileContent {

	private String newContent;

	private String oldContent;

	public FileContent(String oldContent, String newContent) {
		this.oldContent = oldContent;
		this.newContent = newContent;
	}

	public String getNewContent() {
		return newContent;
	}

	public String getOldContent() {
		return oldContent;
	}

}
