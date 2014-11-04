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
package org.flowerplatform.codesync.regex.remote;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END_C;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.END_L;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.FULL_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIG_FILE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_FILES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_RESULT_FILES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.RESOURCE_URI;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START_C;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START_L;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.VIRTUAL_NODE_SCHEME;
import static org.flowerplatform.core.file.FileControllerUtils.getFileAccessController;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.codesync.regex.State;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.config_processor.ConfigProcessor;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.core.node.resource.ResourceSetService;
import org.flowerplatform.core.node.resource.VirtualNodeResourceHandler;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexProcessingSession;
import org.flowerplatform.util.regex.RegexWithActions;

/**
 * @author Cristina Constantinescu
 */
public class CodeSyncRegexService {

	private final Pattern newLinePattern = Pattern.compile("(\r\n)|(\n)|(\r)");
		
	/**
	 *@author see class
	 **/
	 public List<Pair<String, String>> getRegexActions() {
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

		for (RegexAction regexAction : CodeSyncRegexPlugin.getInstance().getActions().values()) {
			list.add(new Pair<String, String>(regexAction.getName(), regexAction.getDescription()));
		}
		return list;
	}

	/**
	 *@author see class
	 **/
	 private void parseFile(String testFilesFolderPath, String testFileToBeParsed, Node resourceNode, RegexConfiguration regexConfig) throws Exception {
		final NodeService nodeService = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> matchFilesContext  = new ServiceContext<NodeService>(nodeService);
		ServiceContext<NodeService> resultFilesContext = new ServiceContext<NodeService>(nodeService);
		
		IFileAccessController fileController = FileControllerUtils.getFileAccessController();

		String testFilePath = testFilesFolderPath + "/" + testFileToBeParsed;
		String testNodeUri = FileControllerUtils.createFileNodeUri(CoreUtils.getRepoFromNode(resourceNode), testFilePath);
		Object testFile = fileController.getFile(FileControllerUtils.getFilePathWithRepo(testNodeUri));
		final String testFileContent = IOUtils.toString((InputStream) fileController.getContent(testFile));

		// create matches file
		Node matchFile = new Node(null, CoreConstants.FILE_NODE_TYPE);
		Node resultFile = new Node(null, CoreConstants.FILE_NODE_TYPE);

		String matchFileName = String.format("%s.regexMatches", testFileToBeParsed);
		matchFilesContext.getContext().put(NAME, matchFileName);
		String resultFileName = String.format("%s.result", testFileToBeParsed);
		resultFilesContext.getContext().put(NAME, resultFileName);

		matchFilesContext.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);
		matchFilesContext.getContext().put(CoreConstants.OVERWRITE_IF_NECESSARY, true);

		resultFilesContext.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);
		resultFilesContext.getContext().put(CoreConstants.OVERWRITE_IF_NECESSARY, true);

		// get regexConfig file
		Object file = fileController.getFile(FileControllerUtils.getFilePathWithRepo(resourceNode));

		// get parent (matches file will be created under match-files, under parent)
		Object parentFile = fileController.getParentFile(file);
		String technologyFilePath = fileController.getPath(parentFile);

		String matchFilesFolderPath = technologyFilePath + "/" + REGEX_MATCH_FILES_FOLDER;
		String matchFilesNodeUri = FileControllerUtils.createFileNodeUri(CoreUtils.getRepoFromNode(resourceNode), matchFilesFolderPath);

		String resultFilesFolderPath = technologyFilePath + "/" + REGEX_RESULT_FILES_FOLDER;
		String resultFilesNodeUri = FileControllerUtils.createFileNodeUri(CoreUtils.getRepoFromNode(resourceNode), resultFilesFolderPath);

		Node matchFilesParent = CorePlugin.getInstance().getResourceService().getNode(matchFilesNodeUri);
		Node resultFilesParent = CorePlugin.getInstance().getResourceService().getNode(resultFilesNodeUri);


		nodeService.addChild(matchFilesParent, matchFile, matchFilesContext);
		nodeService.addChild(resultFilesParent, resultFile, resultFilesContext);

		// subscribe file using fpp schema
		String matchUri = matchFile.getNodeUri().replaceFirst(FILE_SCHEME, "fpp");
		new ResourceServiceRemote().subscribeToParentResource(matchUri);
		CorePlugin.getInstance().getResourceSetService().reload(matchUri, new ServiceContext<ResourceSetService>());

		String resultUri = resultFile.getNodeUri().replaceFirst(FILE_SCHEME, "fpp");
		new ResourceServiceRemote().subscribeToParentResource(resultUri);
		CorePlugin.getInstance().getResourceSetService().reload(resultUri, new ServiceContext<ResourceSetService>());

		// get matches root node & save the textNodeUri as property
		final Node matchRoot = CorePlugin.getInstance().getResourceService().getNode(matchUri);
		nodeService.setProperty(matchRoot, RESOURCE_URI, testNodeUri, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(matchRoot, SHOW_GROUPED_BY_REGEX, false, new ServiceContext<NodeService>(nodeService));

		// same for result root node
		final Node resultRoot = CorePlugin.getInstance().getResourceService().getNode(resultUri);
		nodeService.setProperty(resultRoot, RESOURCE_URI, testNodeUri, new ServiceContext<NodeService>(nodeService));

		// start session
		final RegexProcessingSession session = regexConfig.startSession(testFileContent);

		session.context.put("stateStack", new ArrayList<Object>());

		((ArrayList<Object>) session.context.get("stateStack")).add(0, new State(0, resultRoot));
		// find matches
		session.find(new Runnable() {
			int currentIndex = 1;

			@Override
			public void run() {
				// match found => create node
				Node match = new Node(null, REGEX_MATCH_TYPE);
				RegexWithActions regex = (RegexWithActions) session.getCurrentRegex();
				ServiceContext<NodeService> context = new ServiceContext<NodeService>();
				context.getContext().put(NAME, String.format("%s) %s", String.valueOf(currentIndex++), session.getCurrentRegex().getName()));
				context.getContext().put(FULL_REGEX, regex.getRegex());
				context.getContext().put(REGEX_NAME, regex.getName());

				int[] start = formatIndex(testFileContent, session.getMatcher().start());
				context.getContext().put(START, String.format("L%d C%d", start[0], start[1]));
				context.getContext().put(START_L, start[0]);
				context.getContext().put(START_C, start[1]);

				int[] end = formatIndex(testFileContent, session.getMatcher().end());
				context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
				context.getContext().put(END_L, end[0]);
				context.getContext().put(END_C, end[1]);

				nodeService.addChild(matchRoot, match, context);

				if (session.getCurrentSubMatchesForCurrentRegex() != null) {
					// has subMatches -> add them too as children
					for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
						int index = session.getCurrentMatchGroupIndex() + i + 1;

						context = new ServiceContext<NodeService>();
						context.getContext().put(NAME, session.getCurrentSubMatchesForCurrentRegex()[i]);

						int startIndex = session.getMatcher().start(index);

						if (startIndex == -1) {
							continue;
						}

						start = formatIndex(testFileContent, startIndex);
						context.getContext().put(START, String.format("L%d C%d", start[0], start[1]));
						context.getContext().put(START_L, start[0]);
						context.getContext().put(START_C, start[1]);

						int endIndex = session.getMatcher().end(index);
						end = formatIndex(testFileContent, endIndex);
						context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
						context.getContext().put(END_L, end[0]);
						context.getContext().put(END_C, end[1]);
						nodeService.addChild(match, new Node(null, REGEX_MATCH_TYPE), context);
					}
				}
			}
		});

		// everything worked ok => save file
		CorePlugin.getInstance().getResourceService().save(matchUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		// same for result file
		CorePlugin.getInstance().getResourceService().save(resultUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));


	}
	/**
	 * @param nodeUri
	 * @throws Exception
	 * @author Elena Posea
	 */
	public void generateMatchesForSelection(String nodeUri) throws Exception {
		IFileAccessController fileController = FileControllerUtils.getFileAccessController();
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String typeSpecificPart = virtualNodeHandler.getTypeSpecificPartFromNodeUri(nodeUri);
		int separatorIndex = typeSpecificPart.indexOf('$');
		String technology = typeSpecificPart.substring(0, separatorIndex);
		String testFileRelativePath = typeSpecificPart.substring(separatorIndex + 1);
		String resourceNodeUri = new String(nodeUri);
		resourceNodeUri = resourceNodeUri.replaceFirst(CoreConstants.VIRTUAL_NODE_SCHEME, "fpp");
		int index = resourceNodeUri.indexOf('|');
		resourceNodeUri = resourceNodeUri.substring(0, index + 1);
		resourceNodeUri += REGEX_CONFIGS_FOLDER + "/" + technology + "/" + REGEX_CONFIG_FILE; 

		new ResourceServiceRemote().subscribeToParentResource(resourceNodeUri);
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(resourceNodeUri);
		// get regexConfig file
		Object file = fileController.getFile(FileControllerUtils.getFilePathWithRepo(resourceNode));

		// get parent (matches file will be created under match-files, under
		// parent)
		Object parentFile = fileController.getParentFile(file);

		// create regEx configuration
		RegexConfiguration regexConfig = new RegexConfiguration();
		new ConfigProcessor().processConfigHierarchy(resourceNode, regexConfig);

		regexConfig.compile(Pattern.DOTALL);

		// get text file & content
		String testFilesFolderPath = fileController.getPath(parentFile) + "/" + REGEX_TEST_FILES_FOLDER;
		parseFile(testFilesFolderPath, testFileRelativePath, resourceNode, regexConfig);
	}

	/**
	 * @param nodeUri
	 * @throws Exception
	 * @author Elena Posea
	 */
	public void generateMatchesForAll(String nodeUri) throws Exception {
		IFileAccessController fileController = FileControllerUtils.getFileAccessController();

		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String technology = virtualNodeHandler.getTypeSpecificPartFromNodeUri(nodeUri);
		String resourceNodeUri = new String(nodeUri);
		resourceNodeUri = resourceNodeUri.replaceFirst(CoreConstants.VIRTUAL_NODE_SCHEME, "fpp");
		int index = resourceNodeUri.indexOf('|');
		resourceNodeUri = resourceNodeUri.substring(0, index + 1);
		resourceNodeUri += REGEX_CONFIGS_FOLDER + "/" + technology + "/" + REGEX_CONFIG_FILE; 

		new ResourceServiceRemote().subscribeToParentResource(resourceNodeUri);
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(resourceNodeUri);
		// get regexConfig file
		Object file = fileController.getFile(FileControllerUtils.getFilePathWithRepo(resourceNode));

		// get parent (matches file will be created under match-files, under parent)
		Object parentFile = fileController.getParentFile(file);

		// create regEx configuration
		RegexConfiguration regexConfig = new RegexConfiguration();
		new ConfigProcessor().processConfigHierarchy(resourceNode, regexConfig);

		regexConfig.compile(Pattern.DOTALL);

		// get text file & content
		String testFilesFolderPath = fileController.getPath(parentFile) + "/" + REGEX_TEST_FILES_FOLDER;
		Object folder = null;
		try {
			folder = getFileAccessController().getFile(testFilesFolderPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		List<String> testFiles = getTestFilesRelativeToFolder((File) folder, "");
		for (String testFileToBeParsed : testFiles) {
			parseFile(testFilesFolderPath, testFileToBeParsed, resourceNode, regexConfig);
		}
	}


	/**
	 * @param nodeUri
	 * @return compare results
	 * @throws Exception
	 * @author Elena Posea
	 */
	public String testMatchesForAll(String nodeUri) throws Exception {
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String technology = virtualNodeHandler.getTypeSpecificPartFromNodeUri(nodeUri);
		String repo = CoreUtils.getRepoFromNodeUri(nodeUri);
		String compareResult = "";
		
		String testFilesNodeUri = virtualNodeHandler.createVirtualNodeUri(repo, REGEX_TEST_FILES_NODE_TYPE, technology); 
		Node testFilesNode = CorePlugin.getInstance().getResourceService().getNode(testFilesNodeUri);
		List<Node> testFilesNodeChildren = CorePlugin.getInstance().getNodeService().getChildren(testFilesNode, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));

		compareResult += "Comparing match files...\n";
		for (Node testFileNode : testFilesNodeChildren) {
			compareResult += checkTestFileNodeForMatches(testFileNode);
		}

		compareResult += "\nComparing result files...\n";
		for (Node testFileNode : testFilesNodeChildren) {
			compareResult += checkTestFileNodeForResults(testFileNode);
		}

		return compareResult;
	}

	/**
	 * @param nodeUri
	 * @return compare results
	 * @throws Exception
	 * @author Elena Posea
	 */
	public String testMatchesForSelection(String nodeUri) throws Exception {
		Node testFileNode = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		return "Matches: " + checkTestFileNodeForMatches(testFileNode) + "Results: " + checkTestFileNodeForResults(testFileNode);
	}

	private String checkTestFileNodeForMatches(Node testFileNode) {
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String typeSpecficPart = virtualNodeHandler.getTypeSpecificPartFromNodeUri(testFileNode.getNodeUri());
		String repo = CoreUtils.getRepoFromNodeUri(testFileNode.getNodeUri());
		
		String matchesNodeUri = CoreUtils.createNodeUriWithRepo(VIRTUAL_NODE_SCHEME, repo, REGEX_MATCHES_NODE_TYPE + "@" + typeSpecficPart); 
		Node matchesNode = CorePlugin.getInstance().getResourceService().getNode(matchesNodeUri);

		String expectedMatchesNodeUri = CoreUtils.createNodeUriWithRepo(VIRTUAL_NODE_SCHEME, repo, REGEX_EXPECTED_MATCHES_NODE_TYPE + "@" + typeSpecficPart);
		Node expectedMatchesNode = CorePlugin.getInstance().getResourceService().getNode(expectedMatchesNodeUri);

		// take the two nodes that you want to check
		return compareNodes(matchesNode, expectedMatchesNode);
	}

	private String checkTestFileNodeForResults(Node testFileNode) {
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String typeSpecficPart = virtualNodeHandler.getTypeSpecificPartFromNodeUri(testFileNode.getNodeUri());
		String repo = CoreUtils.getRepoFromNodeUri(testFileNode.getNodeUri());
		
		String modelTreeNodeUri = CoreUtils.createNodeUriWithRepo(VIRTUAL_NODE_SCHEME, repo, REGEX_MODEL_TREE_NODE_TYPE + "@" + typeSpecficPart); 
		Node modelTreeNode = CorePlugin.getInstance().getResourceService().getNode(modelTreeNodeUri);

		String expectedModelTreeNodeUri = CoreUtils.createNodeUriWithRepo(VIRTUAL_NODE_SCHEME, repo, REGEX_EXPECTED_MODEL_TREE_NODE_TYPE + "@" + typeSpecficPart);
		Node expectedModelTreeNode = CorePlugin.getInstance().getResourceService().getNode(expectedModelTreeNodeUri);

		// take the two nodes that you want to check
		return compareNodes(modelTreeNode, expectedModelTreeNode);
	}

	
	private String compareNodes(Node matchesNode, Node expectedMatchesNode) {
		String partialCompareResult = "	Comparing files " + getRelativePathFromUri(matchesNode.getNodeUri()) + " vs " + getRelativePathFromUri(expectedMatchesNode.getNodeUri());
		if (recursiveCompare(matchesNode, expectedMatchesNode, true)) {
			partialCompareResult += " IDENTICAL\n";
		} else {
			partialCompareResult += " NOT THE SAME\n";
		}
		return partialCompareResult;
	}
	
	private String getRelativePathFromUri(String nodeUri) {
		int index = nodeUri.indexOf('$');
		return nodeUri.substring(index + 1);
	}
	
	private boolean recursiveCompare(Node matchFileNode, Node expectedMatchFileNode, boolean firstLevel) {
		ResourceServiceRemote rsr = new ResourceServiceRemote(); 
		rsr.subscribeToParentResource(matchFileNode.getNodeUri());
		List<Node> matchChildren = CorePlugin.getInstance().getNodeService().getChildren(matchFileNode, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		rsr.subscribeToParentResource(expectedMatchFileNode.getNodeUri());
		List<Node> expectedMatchChildren = CorePlugin.getInstance().getNodeService()
				.getChildren(expectedMatchFileNode, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		// compare properties for current node
		if (!firstLevel && (!compareProperties(matchFileNode, expectedMatchFileNode))) { // firstLevel means "Matches" vs "Expected Matches"
			return false;
		}
		int noOfMatchChildren = matchChildren.size();
		int noOfExpectedMatchChildren = expectedMatchChildren.size();
		// then compare its children
		if (noOfMatchChildren != noOfExpectedMatchChildren) {
			return false;
		}
		for (int i = 0; i < noOfMatchChildren; i++) {
			if (!recursiveCompare(matchChildren.get(i), expectedMatchChildren.get(i), false)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean compareProperties(Node matchFileNode, Node expectedMatchFileNode) {
		// iterate through all expectedMatchFileNode's properties, including node type;
		// look for all this properties in matchFileNode
		ServiceContext<NodeService> context = new ServiceContext<>();
		Map<String, Object> expectedProperties = expectedMatchFileNode.getOrPopulateProperties(context);
		Map<String, Object> realProperties = matchFileNode.getOrPopulateProperties(context);
		for (String key : expectedProperties.keySet()) {
			Object realPropertyValue = realProperties.get(key);
			Object expectedPropertyValue = expectedProperties.get(key);
			if (realPropertyValue == null) {
				if (expectedPropertyValue != null) {
					return false;
				} else {
					continue;
				}
			}
			if (!realPropertyValue.equals(expectedPropertyValue)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param folder
	 * @param relativePath
	 * @return a list of all files in that folder, with the appended relativePath before each of them
	 */
	public static List<String> getTestFilesRelativeToFolder(File folder, String relativePath) {
		Object[] files = getFileAccessController().listFiles(folder);
		List<String> allFilesStartingWithThisFolder = new ArrayList<String>();
		if (files != null) {
			for (Object testFileToBeParsed : files) {
				File realFile = (File) testFileToBeParsed;
				if (realFile.isDirectory()) {
					allFilesStartingWithThisFolder.addAll(getTestFilesRelativeToFolder(realFile, relativePath + realFile.getName() + "/"));
				} else {
					allFilesStartingWithThisFolder.add(relativePath + realFile.getName());
				}
			}
		}
		return allFilesStartingWithThisFolder;
	}

	private int[] formatIndex(String str, int index) {
		String prefix = str.substring(0, index);

		Matcher m = newLinePattern.matcher(prefix); // search for new line
													// separator
		int lines = 0;
		int lineEndIndex = -1;
		while (m.find()) {
			lines++;
			lineEndIndex = m.end();
		}
		return new int[] { lines, (lineEndIndex == -1 ? index : index - lineEndIndex) };
	}

}
