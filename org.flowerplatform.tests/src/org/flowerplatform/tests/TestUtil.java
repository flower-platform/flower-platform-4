/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author Cristi
 * @author Sorin
 */
//CHECKSTYLE:OFF
public class TestUtil {
//CHECKSTYLE:ON
	public static final String NORMAL = "normal";
	
	public static final String EXPECTED = "expected";
	
	public static final String INITIAL_TO_BE_COPIED = "initial_to_be_copied";
	
	/**
	 * @author Mariana Gheorghe
	 */
	public static String getResourcesDir(Class<?> cls) {
		return "src/" + cls.getPackage().getName().replaceAll("\\.", "/") + "/resources/";
	}	
	
	/**
	 * @author see class
	 */
	public static String getWorkspaceResourceAbsolutePath(String pathWithinWorkspace) {
		return ResourcesPlugin.getWorkspace().getRoot().findMember(pathWithinWorkspace).getLocation().toString();
	}
	
	public static String getWorkspacePath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}
	
	/**
	 * Copies the files from the specified folder into ws/root/projectName, and imports this as a project.
	 * projectName may contain a leading /.
	 * 
	 * @from Can be null; an empty project will be created.
	 */
	public static final void copyFilesAndCreateProject(String from, String projectName) {
		try {
//			new DatabaseOperationWrapper(new DatabaseOperation() {
//				
//				@Override
//				public void run() {
//					new GeneralService().createOrganization("org", wrapper);
//				}
//			});
//			
//			if (projectName.startsWith("/")) {
//				projectName = projectName.substring(1);
//			}
//			
			File to = new File(getWorkspaceResourceAbsolutePath("") + "/" + projectName);
			if (from != null) {
				FileUtils.copyDirectory(new File(from), to);
			} else {
				to.mkdirs();
			}
//			, new FileFilter() {
//				
//				@Override
//				public boolean accept(File pathname) {
//					if (pathname.getName().endsWith(".svn")) {
//						return false;
//					} else {
//						return true;
//					}
//				}
//			});
			
//			WebWorkspace.INSTANCE.getRoot().refreshLocal(IResource.DEPTH_ONE, null);
//			IFolder projectFolderWithinRootProject = WebWorkspace.INSTANCE.getRoot().getFolder(new Path(projectName));
//			WebWorkspace.INSTANCE.importProject(projectFolderWithinRootProject, projectFolderWithinRootProject.getLocationURI());
			
//			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_ONE, null);
//			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//			IProjectDescription pd = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
//			project.create(pd, null);
//			project.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
//			project.open(IResource.BACKGROUND_REFRESH, null);
//			project.getParent().refreshLocal(IResource.DEPTH_INFINITE, null);
//			
//			if (ProjectsService.getInstance().getWorkingDirectoriesForOrganizationName("org").size() == 0) {
//				ProjectsService.getInstance().markAsWorkingDirectoryForFile(context, to.getParentFile());
//			}
//			ProjectsService.getInstance().createOrImportProjectFromFile(context, to);
//			
		} catch (Throwable e) {
			throw new RuntimeException("Cannot copy files/create project needed for test", e);
		}
	}
	
	/**
	 * Copied from http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
	 */
	public static String readFile(String path) {
		StringBuffer loadedContent = new StringBuffer();
		try {
			InputStreamReader fileEditorInputReader = new InputStreamReader(new FileInputStream(path));
			char[] buffer = new char[1024];
			int bytesRead; 
			do {
				bytesRead = fileEditorInputReader.read(buffer);
				if (bytesRead > 0) {
					loadedContent.append(buffer, 0, bytesRead);
				}				
			} while (bytesRead > 0);
			fileEditorInputReader.close();
		} catch (Exception e) {
			throw new RuntimeException("Error while loading file content " + path, e);
		}	
		return loadedContent.toString();
	}

//	public static Object getRecordedCommandAtIndex(IRecordingTestWebCommunicationChannelProvider context, int commandIndex) {
//		if (context.getRecordingTestWebCommunicationChannel().getRecordedCommands().size() <= commandIndex) {
//			Assert.fail("We are trying to access command #" + commandIndex + " but there are only "
//	+ context.getRecordingTestWebCommunicationChannel().getRecordedCommands().size() + " recorded commands");
//		}
//		return context.getRecordingTestWebCommunicationChannel().getRecordedCommands().get(commandIndex);
//	}
//	
//	/**
//	 * Useful when there may be commands in channel from previous test. 
//	 * @see #assertExist_InvokeStatefulClientMethodClientCommand() 
//	 */
//	public static InvokeStatefulClientMethodClientCommand assertInvokeStatefulClientMethodClientCommand(Object obj, int commandIndex, String statefulClientId, String methodName) {
//		if (obj instanceof IRecordingTestWebCommunicationChannelProvider) {
//			obj = getRecordedCommandAtIndex((IRecordingTestWebCommunicationChannelProvider) obj, commandIndex);
//		}
//		if (!(obj instanceof InvokeStatefulClientMethodClientCommand)) {
//			Assert.fail("We were expecting a " + InvokeStatefulClientMethodClientCommand.class.getSimpleName() + " but we got a " + obj.getClass().getSimpleName());
//		}
//		InvokeStatefulClientMethodClientCommand command = (InvokeStatefulClientMethodClientCommand) obj;
//		Assert.assertEquals("Invoked client is not correct", statefulClientId, command.getStatefulClientId());
//		Assert.assertEquals("Invoked client method is not correct", methodName, command.getMethodName());
//		return command;
//	}
//	
//	/**
//	 * Useful when there may be commands in channel from previous test. 
//	 * @see #assertExist_DisplaySimpleMessageCommand() 
//	 */
//	public static void assertDisplaySimpleMessageCommand(Object obj, int commandIndex, String message, boolean useContainsMatch) {
//		if (obj instanceof TestStatefulServiceInvocationContext) {
//			obj = getRecordedCommandAtIndex((TestStatefulServiceInvocationContext) obj, commandIndex);
//		}
//		if (!(obj instanceof DisplaySimpleMessageClientCommand)) {
//			Assert.fail("We were expecting a " + InvokeStatefulClientMethodClientCommand.class.getSimpleName() + " but we got a " + obj.getClass().getSimpleName());
//		}
//		DisplaySimpleMessageClientCommand command = (DisplaySimpleMessageClientCommand) obj;
//		if (!useContainsMatch) {
//			// should be exact match
//			Assert.assertEquals("Sent message is not correct", message, command.getMessage());
//		} else {
//			Assert.assertTrue(String.format("The message %s should contain %s", command.getMessage(), message), command.getMessage().contains(message));
//		}
//	}
	
	/**
	 * @author see class
	 */
	public static void createDirectoriesIfNeeded(String path) {
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
	}
	
	/**
	 * @author see class
	 */
	public static String getCanonicalPath(String path) {
		try {
			return new File(path).getCanonicalPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
//	/**
//	 * Be careful not to have commands from previous test in channel (make a new one!)
//	 */
//	public static void assertExist_CreateEditorStatefulClientCommand(IRecordingTestWebCommunicationChannelProvider recordingChannelProvider,
//	String editor, String editableResourcePath) {
//		CreateEditorStatefulClientCommand foundCommand = null;
//		for (Object obj : recordingChannelProvider.getRecordingTestWebCommunicationChannel().getRecordedCommands()) {
//			if (obj instanceof CreateEditorStatefulClientCommand) {
//				CreateEditorStatefulClientCommand command = (CreateEditorStatefulClientCommand) obj;
//				if (areEqual(editableResourcePath, command.getEditableResourcePath())) {
//					foundCommand = command;
//					break;
//				}
//			}
//		}
//
//		assertNotNull(editableResourcePath + " was not opened", foundCommand);
//		assertEquals(editableResourcePath + " was opened but with different editor ", editor, foundCommand.getEditor());
//	}
//	
//	/**
//	 * Be careful not to have commands from previous test in channel (make a new one!)
//	 *  
//	 * @param recordingChannelProvider
//	 * @param message mandatory
//	 * @param details optional
//	 * @param useContainsMatch whether to use contain match on message and on details (if present) 
//	 */
//	public static void assertExist_DisplaySimpleMessageCommand(IRecordingTestWebCommunicationChannelProvider recordingChannelProvider, String message,
//	String details, boolean useContainsMatch) {
//		DisplaySimpleMessageClientCommand foundCommand = null;
//		for (Object obj : recordingChannelProvider.getRecordingTestWebCommunicationChannel().getRecordedCommands()) {
//			if (obj instanceof DisplaySimpleMessageClientCommand) {
//				DisplaySimpleMessageClientCommand command = (DisplaySimpleMessageClientCommand) obj;
//				if (command.getMessage().contains(message) &&
//						details != null && command.getDetails() != null && command.getDetails().contains(details)) {
//					foundCommand = command;
//					break;
//				}
//			}
//		}
//		
//		assertNotNull(format("Could not find DisplaySimpleMessageCommand for message %s with details method %s using %s", message, details, useContainsMatch), foundCommand);
//	}
//	
//	/**
//	 * Be careful not to have commands from previous test in channel (make a new one!)
//	 */
//	public static InvokeStatefulClientMethodClientCommand assertExist_InvokeStatefulClientMethodClientCommand(IRecordingTestWebCommunicationChannelProvider 
//	recordingChannelProvider,
//	String statefulClientId, String methodName) {
//		InvokeStatefulClientMethodClientCommand foundCommand = null;
//		for (Object obj : recordingChannelProvider.getRecordingTestWebCommunicationChannel().getRecordedCommands()) {
//			if (obj instanceof InvokeStatefulClientMethodClientCommand) {
//				InvokeStatefulClientMethodClientCommand command = (InvokeStatefulClientMethodClientCommand) obj;
//				if (areEqual(statefulClientId, command.getStatefulClientId()) && areEqual(methodName, command.getMethodName())) {
//					foundCommand = command;
//					break;
//				}
//			}
//		}
//		assertNotNull(format("Could not find InvokeStatefulClientMethodClientCommand for client %s with client method %s", statefulClientId, methodName), foundCommand);
//		return foundCommand;
//	}
//	
//	// TODO Sorin : assertExist - de unificat cu visitator
//
//	private static boolean areEqual(String s1, String s2) {
//		return s1 != null ? s1.equals(s2) : s2 == null;
//	}
//	
//	public static String computeEditorStatefulClientId(String editor, String editableResourcePath) {
//		EditorStatefulService editorStatefulService = (EditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry()
//				.getService("");
//		return editorStatefulService.calculateStatefulClientId(editableResourcePath);
//	}
	
}