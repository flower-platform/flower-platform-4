package org.flowerplatform.tests.controllers;

import static org.flowerplatform.core.node.controller.AddNodeController.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.node.controller.ChildrenProvider.CHILDREN_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertiesProvider.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.node.controller.PropertySetter.PROPERTY_SETTER;
import static org.flowerplatform.core.node.controller.RemoveNodeController.REMOVE_NODE_CONTROLLER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileAddNodeController;
import org.flowerplatform.core.file.FileChildrenProvider;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.file.FilePropertySetter;
import org.flowerplatform.core.file.FileRemoveNodeController;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Sebastian Solomon
 */
public class FileSystemControllersTest {
	private static NodeService nodeService;
	private static IFileAccessController fileAccessController;
	
	private String fileSystemNode;
	private String initialToBeCopied;
	
	@BeforeClass
	public static void beforeClass() {
		CorePlugin mockCorePlugin = mock(CorePlugin.class);
		
		Field field;
		try {
			field = CorePlugin.class.getDeclaredField("INSTANCE");
			field.setAccessible(true);
			field.set(null, mockCorePlugin);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		fileAccessController = new PlainFileAccessController();
		when(mockCorePlugin.getFileAccessController()).thenReturn(fileAccessController);
		
		TypeDescriptorRegistry descriptorRegistry = new TypeDescriptorRegistry();
		nodeService = new NodeService(descriptorRegistry);
		when(mockCorePlugin.getNodeService()).thenReturn(nodeService);
		
		when(mockCorePlugin.getNodeTypeDescriptorRegistry()).thenReturn(descriptorRegistry);
		TypeDescriptor fileNodeTypeDescriptor = descriptorRegistry
								.getOrCreateTypeDescriptor("fileNode");
		
		fileNodeTypeDescriptor.addAdditiveController(CHILDREN_PROVIDER, new FileChildrenProvider());
		fileNodeTypeDescriptor.addAdditiveController(PROPERTIES_PROVIDER, new FilePropertiesProvider());
		fileNodeTypeDescriptor.addAdditiveController(ADD_NODE_CONTROLLER, new FileAddNodeController());
		fileNodeTypeDescriptor.addAdditiveController(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileNodeTypeDescriptor.addAdditiveController(PROPERTY_SETTER, new FilePropertySetter());
	}
	
	@Before
	public void setUp() {
		File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		f= f.getParentFile();
		fileSystemNode = f.getPath() + "\\temp\\fileSystemNode";
		initialToBeCopied = f.getPath()  + "\\src\\org\\flowerplatform\\tests\\controllers\\resources\\initial_to_be_copied";
		
		fileAccessController.deleteFolderContent(new File(fileSystemNode));
		try {
			copyDirectory(new File(initialToBeCopied), new File(fileSystemNode));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChildren() {
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode, null), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\1", null),
								new Node("fileNode", null, fileSystemNode + "\\A", null),
								new Node("fileNode", null, fileSystemNode + "\\B", null)));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A", null), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\A\\file1", null),
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder1", null),
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder2", null)));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder1", null), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder1\\oneFile", null)));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2", null), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder2\\oneFolder", null)));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2\\oneFolder", null), false),
								Arrays.asList());
	}
	
	@Test
	public void addChild() {
		NodeServiceRemote nodeServiceRemote = new NodeServiceRemote();
		//add file
		HashMap<String, Object> fileProperties = new HashMap<String, Object>();
		fileProperties.put("type", CorePlugin.FILE_NODE_TYPE);
		fileProperties.put("text", "newFile");
		fileProperties.put("isDirectory", false);
		
		String fullNodeId = new Node("fileNode", null, fileSystemNode + "\\A\\Folder1", null).getFullNodeId();
	        
		nodeServiceRemote.addChild(fullNodeId, fileProperties, null);
							 
		Object newFile;
		try {
			newFile = fileAccessController.getFile(fileSystemNode + "\\A\\Folder1\\newFile");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// test if the needed file was created on disk
		assertEquals(fileAccessController.exists(newFile), true);
		assertEquals(fileAccessController.isDirectory(newFile), false);
		
		//add folder
		HashMap<String, Object> folderProperties = new HashMap<String, Object>();
		folderProperties.put("type", CorePlugin.FILE_NODE_TYPE);
		folderProperties.put("text", "newFolder");
		folderProperties.put("isDirectory", true);
		
		fullNodeId = new Node("fileNode",null , fileSystemNode + "\\A\\Folder1", null).getFullNodeId();
		nodeServiceRemote.addChild(fullNodeId, folderProperties, null);
		Object newFolder;
		try {
			newFolder = fileAccessController.getFile(fileSystemNode + "\\A\\Folder1\\newFolder");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// test if the needed directory was created on disk
		assertEquals(fileAccessController.exists(newFolder), true);
		assertEquals(fileAccessController.isDirectory(newFolder), true);
	}
	
	@Test
	public void removeNode() {
		//delete oneFolder
		nodeService.removeChild(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2", null), 
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder2\\oneFolder", null));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2", null), false), 
								Arrays.asList());
		Object newFolder;
		try {
			newFolder = fileAccessController.getFile(fileSystemNode + "\\A\\Folder2\\oneFolder");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		assertEquals(fileAccessController.exists(newFolder), false);
	}

	public void copyDirectory(File srcPath, File dstPath) throws IOException {
		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
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
