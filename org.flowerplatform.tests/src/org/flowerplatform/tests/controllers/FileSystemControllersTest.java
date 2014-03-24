package org.flowerplatform.tests.controllers;

import static org.flowerplatform.core.CorePlugin.FILE_NODE_TYPE;
import static org.flowerplatform.core.CorePlugin.FILE_SYSTEM_NODE_TYPE;
import static org.flowerplatform.tests.EclipseIndependentTestSuite.nodeService;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.NodePropertiesConstants;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.tests.EclipseIndependentTestSuite;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sebastian Solomon
 */
public class FileSystemControllersTest {
	
	private static IFileAccessController fileAccessController = new PlainFileAccessController();
	
	private String fileSystemNodeId;
	private String initialToBeCopied;
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		if (CorePlugin.getInstance() == null) {
			EclipseIndependentTestSuite.startPlugin(new CorePlugin());
			nodeService = CorePlugin.getInstance().getNodeService();
		}
	}
	
	@Before
	public void setUp() {
		File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		f= f.getParentFile();
		fileSystemNodeId = f.getPath() + "\\temp\\fileSystemNode";
		initialToBeCopied = f.getPath()  + "\\src\\org\\flowerplatform\\tests\\controllers\\resources\\initial_to_be_copied";
		
		fileAccessController.delete(new File(fileSystemNodeId));
		try {
			copyDirectory(new File(initialToBeCopied), new File(fileSystemNodeId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	@Test
	public void testGetChildren() {
		assertEquals(nodeService.getChildren(new Node(FILE_NODE_TYPE, null, fileSystemNodeId, null), false), Arrays.asList(
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\1", null),
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A", null),
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\B", null)));
		
		assertEquals(nodeService.getChildren(new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A", null), false), Arrays.asList(
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\file1", null),
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder1", null),
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder2", null)));
		
		assertEquals(nodeService.getChildren(new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder1", null), false), Arrays.asList(
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder1\\oneFile", null)));
		
		assertEquals(nodeService.getChildren(new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder2", null), false), Arrays.asList(
								new Node(FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder2\\oneFolder", null)));
	}
	
	@Test
	public void addChild() {
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		//add file
		HashMap<String, Object> fileProperties = new HashMap<String, Object>();
		fileProperties.put("type", CorePlugin.FILE_NODE_TYPE);
		fileProperties.put(NodePropertiesConstants.NAME, "newFile");
		fileProperties.put(NodePropertiesConstants.FILE_IS_DIRECTORY, false);
		String fileSystemFullNodeId = (new Node(FILE_SYSTEM_NODE_TYPE, null, fileSystemNodeId, null)).getFullNodeId();
		String fullNodeId = new Node(FILE_NODE_TYPE, fileSystemFullNodeId, fileSystemNodeId + "\\A\\Folder1", null).getFullNodeId();
	        
		nodeServiceRemote.addChild(fullNodeId, fileProperties, null);
							 
		Object newFile;
		try {
			newFile = fileAccessController.getFile(fileSystemNodeId + "\\A\\Folder1\\newFile");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// test if the needed file was created on disk
		assertEquals(fileAccessController.exists(newFile), true);
		assertEquals(fileAccessController.isDirectory(newFile), false);
		
		//add folder
		HashMap<String, Object> folderProperties = new HashMap<String, Object>();
		folderProperties.put("type", FILE_NODE_TYPE);
		folderProperties.put(NodePropertiesConstants.NAME, "newFolder");
		folderProperties.put(NodePropertiesConstants.FILE_IS_DIRECTORY, true);
		
		fullNodeId = new Node(CorePlugin.FILE_NODE_TYPE, fileSystemFullNodeId, fileSystemNodeId + "\\A\\Folder1", null).getFullNodeId();
		nodeServiceRemote.addChild(fullNodeId, folderProperties, null);
		Object newFolder;
		try {
			newFolder = fileAccessController.getFile(fileSystemNodeId + "\\A\\Folder1\\newFolder");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// test if the needed directory was created on disk
		assertEquals(fileAccessController.exists(newFolder), true);
		assertEquals(fileAccessController.isDirectory(newFolder), true);
	}
	
	@Test
	public void removeNode() {
		String fileSystemFullNodeId = (new Node(FILE_SYSTEM_NODE_TYPE, null, fileSystemNodeId, null)).getFullNodeId();
		//delete oneFolder
		nodeService.removeChild(new Node(CorePlugin.FILE_NODE_TYPE, fileSystemFullNodeId, fileSystemNodeId + "\\A\\Folder2", null), 
								new Node(CorePlugin.FILE_NODE_TYPE, fileSystemFullNodeId, fileSystemNodeId + "\\A\\Folder2\\oneFolder", null));
		
		assertEquals(nodeService.getChildren(new Node(CorePlugin.FILE_NODE_TYPE, null, fileSystemNodeId + "\\A\\Folder2", null), false), 
								Arrays.asList());
		Object newFolder;
		try {
			newFolder = fileAccessController.getFile(fileSystemNodeId + "\\A\\Folder2\\oneFolder");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		assertEquals(fileAccessController.exists(newFolder), false);
	}

	public void copyDirectory(File srcPath, File dstPath) throws IOException {
		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdirs();
			}
			String files[] = srcPath.list();
			for (int i = 0; i < files.length; i++) {
				File src = new File(srcPath, files[i]);
				File dest = new File(dstPath, files[i]);
				copyDirectory(src, dest);
			}
		} else {
			// if file, then copy it
			InputStream in;
			OutputStream out;
			try {
				in = new FileInputStream(srcPath);
				out = new FileOutputStream(dstPath);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			try {
				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				in.close();
				out.close();
			}
		}
	}
	
}
