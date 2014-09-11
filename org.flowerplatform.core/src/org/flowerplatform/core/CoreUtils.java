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
package org.flowerplatform.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.IController;

/**
 * @author Mariana Gheorghe
 */
public final class CoreUtils {

	private CoreUtils() {
		
	}
	
	/**
	 *@author Cristina Constantinescu
	 **/
	public static void delete(File f) {	
		if (f.isDirectory() && !Files.isSymbolicLink(Paths.get(f.toURI()))) {		
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		f.delete();
	}
	
	/**
	 *@author Cristina Constantinescu
	 **/
	public static void zipFiles(List<String> files, String zipFilePath, String rootFolderName) throws Exception {		
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		
		fileWriter = new FileOutputStream(zipFilePath);
		zip = new ZipOutputStream(fileWriter);
		zip.putNextEntry(new ZipEntry(rootFolderName + "/"));
		
		for (String path : files) {
			File file = new File(path);
			if (file.isFile()) {
				addFileToZip(rootFolderName, path, zip, false);
			} else {
				addFolderToZip(rootFolderName, path, zip);
			}
		}
		zip.flush();
		zip.close();	
	}

	private static void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws Exception {		
		File folder = new File(srcFile);
		if (flag) {
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
		} else { 
			if (folder.isDirectory()) {				
				addFolderToZip(path, srcFile, zip);
			} else {				
				byte[] buf = new byte[10240];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {					
					zip.write(buf, 0, len);
				}
			}
		}
	}
	
	private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);
		if (folder.list().length == 0) {			
			addFileToZip(path, srcFolder, zip, true);
		} else {			
			for (String fileName : folder.list()) {
				if (folder.equals(new File(CoreConstants.FLOWER_PLATFORM_WORKSPACE)) && CoreConstants.METADATA.equals(fileName)) {
					continue;
				}
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
				} else {
					addFileToZip(path + "/" + folder.getName(), srcFolder + "/"	+ fileName, zip, false);
				}
			}
		}
	}
	
	/**
	 *@author Cristina Constantinescu
	 **/
	@SuppressWarnings("rawtypes")
	public static void unzipArchive(File archive, File outputDir) throws IOException {
		ZipFile zipfile = new ZipFile(archive);
		for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			unzipEntry(zipfile, entry, outputDir);
		}	
		zipfile.close();
	}

	private static void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {
		if (entry.isDirectory()) {
			new File(outputDir, entry.getName()).mkdirs();
			return;
		}

		File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}

		BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}
	
	/**
	 * @return <tt>scheme:repo|schemeSpecificPart</tt>
	 */
	public static String createNodeUriWithRepo(String scheme, String repo, String schemeSpecificPart) {
		return scheme + ":" + (repo == null ? "" : repo) + "|" + (schemeSpecificPart == null ? "" : schemeSpecificPart);
	}
	
	/**
	 * @see #getRepoFromNodeUri(String)
	 */
	public static String getRepoFromNode(Node node) {
		return getRepoFromNodeUri(node.getNodeUri());
	}
	
	/**
	 * @return <tt>repo</tt> for URI <tt>scheme:repo|schemeSpecificPart</tt>
	 */
	public static String getRepoFromNodeUri(String nodeUri) {
		String ssp = Utils.getSchemeSpecificPart(nodeUri);
		int index = ssp.indexOf("|");
		if (index < 0) {
			return ssp;
		}
		return ssp.substring(0, index);
	}
	
	/**
	 * @return <tt>schemeSpecificPart</tt> for URI <tt>scheme:repo|schemeSpecificPart</tt>
	 */
	public static String getSchemeSpecificPartWithoutRepo(String nodeUri) {
		String ssp = Utils.getSchemeSpecificPart(nodeUri);
		int index = ssp.indexOf("|");
		if (index < 0) {
			return null;
		}
		return ssp.substring(index + 1);
	}

	/**
	 *@author Claudiu Matei
	 **/
	public static boolean isControllerInvokable(IController controller, ServiceContext<?> context) {
		@SuppressWarnings("unchecked")
		List<Class<?>> invokeOnlyControllersWithClasses = (List<Class<?>>) context.get(CoreConstants.INVOKE_ONLY_CONTROLLERS_WITH_CLASSES);
		if (invokeOnlyControllersWithClasses == null) {
			return true;
		}
		for (Class<?> marker : invokeOnlyControllersWithClasses) {
			if (marker.isInstance(controller)) {
				return true;
			}
		}
		return false;
	}
}
