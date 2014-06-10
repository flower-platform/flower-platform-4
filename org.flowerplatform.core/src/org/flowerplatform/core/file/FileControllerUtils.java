package org.flowerplatform.core.file;

import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class FileControllerUtils {

	public static IFileAccessController getFileAccessController() {
		return CorePlugin.getInstance().getFileAccessController();
	}
	
	public static String getRepo(Node node) {
		return getRepo(node.getNodeUri());
	}
	
	public static String getRepo(String nodeUri) {
		String fullPathWithRepo = Utils.getSchemeSpecificPart(nodeUri);
		int index = fullPathWithRepo.indexOf("|");
		if (index < 0) {
			return fullPathWithRepo;
		}
		return fullPathWithRepo.substring(0, index);
	}
	
	public static String getFilePath(Node node) {
		return getFilePath(node.getNodeUri());
	}
	
	public static String getFilePath(String nodeUri) {
		String fullPathWithRepo = Utils.getSchemeSpecificPart(nodeUri);
		int index = fullPathWithRepo.indexOf("|");
		if (index < 0) {
			return null;
		}
		return fullPathWithRepo.substring(index + 1);
	}
	
	public static String createFileNodeUri(String repo, String path) {
		return Utils.getUri(FILE_SCHEME, repo + 
				(path == null ? "" : "|" + path), null);
	}
	
}
