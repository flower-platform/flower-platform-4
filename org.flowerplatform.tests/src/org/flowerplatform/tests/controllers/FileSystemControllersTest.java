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
import org.flowerplatform.core.file.FileAddNodeControlller;
import org.flowerplatform.core.file.FileChildrenProvider;
import org.flowerplatform.core.file.FilePropertiesProvider;
import org.flowerplatform.core.file.FilePropertySetter;
import org.flowerplatform.core.file.FileRemoveNodeController;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.file.PlainFileAccessController;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeService;
import org.flowerplatform.util.type_descriptor.TypeDescriptor;
import org.flowerplatform.util.type_descriptor.TypeDescriptorRegistry;
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
		
		TypeDescriptor fileNodeTypeDescriptor = descriptorRegistry
								.getOrCreateNodeTypeDescriptor("fileNode");
		
		fileNodeTypeDescriptor.addControllerToList(CHILDREN_PROVIDER, new FileChildrenProvider());
		fileNodeTypeDescriptor.addControllerToList(PROPERTIES_PROVIDER, new FilePropertiesProvider());
		fileNodeTypeDescriptor.addControllerToList(ADD_NODE_CONTROLLER, new FileAddNodeControlller());
		fileNodeTypeDescriptor.addControllerToList(REMOVE_NODE_CONTROLLER, new FileRemoveNodeController());
		fileNodeTypeDescriptor.addControllerToList(PROPERTY_SETTER, new FilePropertySetter());
	}
	
	@Before
	public void setUp() {
		File f = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		f= f.getParentFile();
		fileSystemNode = f.getPath() + "\\temp\\fileSystemNode";
		initialToBeCopied = f.getPath()  + "\\src\\org\\flowerplatform\\tests\\controllers\\resources\\initial_to_be_copied";
		
		new FileRemoveNodeController().deleteFolder(new File(fileSystemNode));
		try {
			copyDirectory(new File(initialToBeCopied), new File(fileSystemNode));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetChildren() {
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\1"),
								new Node("fileNode", null, fileSystemNode + "\\A"),
								new Node("fileNode", null, fileSystemNode + "\\B")));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A"), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\A\\file1"),
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder1"),
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder2")));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder1"), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder1\\oneFile")));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2"), false), Arrays.asList(
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder2\\oneFolder")));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2\\oneFolder"), false),
								Arrays.asList());
	}
	
	@Test
	public void addChild() {
		//add file
		HashMap<String, Object> fileProperties = new HashMap<String, Object>();
		fileProperties.put("body", "newFile");
		fileProperties.put("isDirectory", false);
	        
		nodeService.addChild(new Node("fileNode", null, fileSystemNode + "\\A\\Folder1"),
							 new Node("fileNode", null, null, fileProperties));
		Object newFile;
		try {
			newFile = fileAccessController.getFile(fileSystemNode + "\\A\\Folder1\\newFile");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		assertEquals(fileAccessController.exists(newFile), true);
		assertEquals(fileAccessController.isDirectory(newFile), false);
		
		//add folder
		HashMap<String, Object> folderProperties = new HashMap<String, Object>();
		folderProperties.put("body", "newFolder");
		folderProperties.put("isDirectory", true);
		nodeService.addChild(new Node("fileNode",null , fileSystemNode + "\\A\\Folder1"),
				 			 new Node("fileNode", null, null, folderProperties));
		Object newFolder;
		try {
			newFolder = fileAccessController.getFile(fileSystemNode + "\\A\\Folder1\\newFolder");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		assertEquals(fileAccessController.exists(newFolder), true);
		assertEquals(fileAccessController.isDirectory(newFolder), true);
	}
	
	@Test
	public void removeNode() {
		//delete oneFolder
		nodeService.removeChild(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2"), 
								new Node("fileNode", null, fileSystemNode + "\\A\\Folder2\\oneFolder"));
		
		assertEquals(nodeService.getChildren(new Node("fileNode", null, fileSystemNode + "\\A\\Folder2"), false), 
								Arrays.asList());
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
