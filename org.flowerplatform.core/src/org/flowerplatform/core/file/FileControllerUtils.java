/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * license-end
 */
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
	
	public static String getFilePathWithRepo(Node node) {
		return getFilePathWithRepo(node.getNodeUri());
	}
	
	public static String getFilePathWithRepo(String nodeUri) {
		return Utils.getSchemeSpecificPart(nodeUri).replace("|", "/");
	}
	
	public static String getFilePathWithoutRepo(String nodeUri) {
		String fullPathWithRepo = Utils.getSchemeSpecificPart(nodeUri);
		int index = fullPathWithRepo.indexOf("|");
		if (index < 0) {
			return null;
		}
		return fullPathWithRepo.substring(index + 1);
	}
	
	public static String createFileNodeUri(String repo, String path) {
		// remove the repo prefix from the file path
		if (path != null && path.startsWith(repo)) {
			path = path.substring(repo.length() + 1);
		}
		return Utils.getUri(FILE_SCHEME, repo + 
				(path == null ? "" : "|" + path), null);
	}
	
}