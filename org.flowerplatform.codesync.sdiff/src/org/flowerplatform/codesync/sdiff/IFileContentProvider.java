package org.flowerplatform.codesync.sdiff;

/**
 * Interface which contains methods for providing the FileContent of a certain
 * file.
 * 
 * @author Valentina-Camelia Bojan
 */
public interface IFileContentProvider {

	/**
	 * @author see class
	 */
	FileContent getFileContent(String filePath, String repo, Object patch);

}
