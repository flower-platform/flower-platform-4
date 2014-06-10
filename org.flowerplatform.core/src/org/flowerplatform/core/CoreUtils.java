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
import org.flowerplatform.util.Utils;

/**
 * @author Mariana Gheorghe
 */
public class CoreUtils {

	public static void delete(File f) {	
		if (f.isDirectory() && !Files.isSymbolicLink(Paths.get(f.toURI()))) {		
			for (File c : f.listFiles()) {
				delete(c);
			}
		}
		f.delete();
	}
	
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
		if (flag == true) {
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
				if (folder.equals(new File(CorePlugin.getInstance().getWorkspaceLocation())) && CoreConstants.METADATA.equals(fileName)) {
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
	
}
