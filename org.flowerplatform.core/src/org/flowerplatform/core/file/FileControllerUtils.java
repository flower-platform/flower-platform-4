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
import static org.flowerplatform.core.CoreUtils.createNodeUriWithRepo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
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
	
	/**
	 * @author Vlad Bogdan Manica
	 */	
	public static String getFilePathFromNodeUri(String nodeUri) {
		int index = nodeUri.indexOf("|"); 
		if (index < 0) {
			return null;
		} else {
			return nodeUri.substring(index + 1);
		}
	}
	
	public static String getFilePathWithRepo(Node node) {
		return getFilePathWithRepo(node.getNodeUri());
	}
	
	public static String getFilePathWithRepo(String nodeUri) {
		return Utils.getSchemeSpecificPart(nodeUri).replace("|", "/");
	}
	
	public static String createFileNodeUri(String repo, String path) {
		// remove the repo prefix from the file path
		if (path != null && path.startsWith(repo)) {
			path = path.substring(repo.length() + 1);
		}
		return createNodeUriWithRepo(FILE_SCHEME, repo, (path == null ? "" : path));
	}
	
	public static String getNextAvailableName(String filePath) {
		try {
			return getNextAvailableName(filePath, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static String getNextAvailableName(String initialFilePath, int startingIndexSuffix) throws Exception {		
		String fileNameWithoutExtension = FilenameUtils.removeExtension(FilenameUtils.getName(initialFilePath));
		
		String newFileName = String.format("%s%s.%s", fileNameWithoutExtension, startingIndexSuffix == 0 ? "" : String.valueOf(startingIndexSuffix), FilenameUtils.getExtension(initialFilePath));
		String newFilePath = String.format("%s%s", FilenameUtils.getFullPath(initialFilePath), newFileName);
		
		if (!CorePlugin.getInstance().getFileAccessController().exists(CorePlugin.getInstance().getFileAccessController().getFile(newFilePath))) {
			return newFileName;
		}
		
		if (startingIndexSuffix == 0) {
			// first group is name, second group is index
			Pattern pattern = Pattern.compile("(.*?)(\\d*)$");
			Matcher matcher = pattern.matcher(fileNameWithoutExtension);
			if (matcher.find()) {
				startingIndexSuffix = 1;
				if (matcher.group(2).length() > 0) {
					startingIndexSuffix = Integer.parseInt(matcher.group(2));
				}
				startingIndexSuffix++;
			}
		} else {
			startingIndexSuffix++;
		}
		return getNextAvailableName(initialFilePath, startingIndexSuffix);		
	}
	
}