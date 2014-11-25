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

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIGS_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_CONFIG_FILE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_EXPECTED_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCHES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_FILES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MODEL_TREE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_RESULT_FILES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILES_FOLDER;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILES_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TEST_FILE_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.RESOURCE_URI;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
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
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexProcessingSession;

/**
 * @author Cristina Constantinescu
 * @author Elena Posea
 * @author Mariana Gheorghe
 */
public class CodeSyncRegexService {

	/**
	 * @author see class
	 **/
	 public List<Pair<String, String>> getRegexActions() {
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();

		for (RegexAction regexAction : CodeSyncRegexPlugin.getInstance().getActions().values()) {
			list.add(new Pair<String, String>(regexAction.getName(), regexAction.getDescription()));
		}
		return list;
	}

	/**
	 * @param nodeUri
	 * @throws Exception
	 * @author Elena Posea
	 * @author Mariana Gheorghe
	 */
	public void generateMatches(String nodeUri, boolean all) throws Exception {
		IFileAccessController fileController = FileControllerUtils.getFileAccessController();
	
		// get repo and technology from node
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String repo = Utils.getRepo(nodeUri);
		String[] typeSpecificPart = virtualNodeHandler.getTypeSpecificPartFromNodeUri(nodeUri).split("\\$");
		String technology = typeSpecificPart[0];
		String regexUri = FileControllerUtils.createFileNodeUri(
				repo, REGEX_CONFIGS_FOLDER + "/" + technology + "/" + REGEX_CONFIG_FILE);
		regexUri = regexUri.replaceFirst(CoreConstants.FILE_SCHEME, "fpp");
		
		// get regex configuration file
		new ResourceServiceRemote().subscribeToParentResource(regexUri);
		Node regex = CorePlugin.getInstance().getResourceService().getResourceNode(regexUri);
		
		// create regEx configuration
		RegexConfiguration regexConfig = new RegexConfiguration();
		new ConfigProcessor().processConfigHierarchy(regex, regexConfig);
	
		regexConfig.compile(Pattern.DOTALL);
		
		String technologyPath = REGEX_CONFIGS_FOLDER + "/" + technology;
		Node technologyNode = CorePlugin.getInstance().getResourceService().getNode(
				FileControllerUtils.createFileNodeUri(repo, technologyPath));
		if (all) {
			// parse files contained in the test files directory
			Object file = fileController.getFile(FileControllerUtils.getFilePathWithRepo(regex));
			Object parentFile = fileController.getParentFile(file);
			Object testFilesFolder = fileController.getFile(parentFile, REGEX_TEST_FILES_FOLDER);
			List<Object> testFiles = getTestFiles(testFilesFolder);
			for (Object testFile : testFiles) {
				String relativePath = fileController.getPathRelativeToFile(testFile, testFilesFolder);
				parseFile(technologyNode, testFile, relativePath, regex, regexConfig);
			}
		} else {
			// parse only selected file
			String relativePath = typeSpecificPart[1];
			Object testFile = fileController.getFile(repo + "/" + technologyPath + "/" + REGEX_TEST_FILES_FOLDER + "/" + relativePath);
			parseFile(technologyNode, testFile, relativePath, regex, regexConfig);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void parseFile(Node technology, Object testFile, String testFilePath, Node regex, RegexConfiguration regexConfig) throws Exception {
		NodeService nodeService = CorePlugin.getInstance().getNodeService();
		IFileAccessController fileController = FileControllerUtils.getFileAccessController();

		// get test file node and test file resource uri
		Node testNode = CorePlugin.getInstance().getResourceService().getNode(
				FileControllerUtils.createFileNodeUri(Utils.getRepo(technology.getNodeUri()), fileController.getPath(testFile)));
		List subscribableResources = (List) testNode.getPropertyValue(CoreConstants.SUBSCRIBABLE_RESOURCES);
		String testResourceUri = (String) ((Pair) subscribableResources.get(0)).a;
		
		// start session
		String testFileContent = IOUtils.toString((InputStream) fileController.getContent(testFile));
		RegexProcessingSession session = regexConfig.startSession(testFileContent);

		// prepare to collect model tree
		Node resultRoot = createResourceRoot(technology, String.format(
				REGEX_RESULT_FILES_FOLDER + "/%s.result", testFilePath), testResourceUri);
		ArrayList<Object> stateStack = new ArrayList<Object>();
		stateStack.add(0, new State(0, resultRoot));
		session.context.put(CodeSyncRegexConstants.STATE_STACK, stateStack);
		
		// prepare to collect match tree
		Node matchRoot = createResourceRoot(technology, String.format(
				REGEX_MATCH_FILES_FOLDER + "/%s.regexMatches", testFilePath), testResourceUri);
		session.find(new CodeSyncRegexMatcher(nodeService, session, testFileContent, matchRoot));

		// everything worked ok => save matches and result
		CorePlugin.getInstance().getResourceService().save(matchRoot.getNodeUri(), new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		CorePlugin.getInstance().getResourceService().save(resultRoot.getNodeUri(), new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
	}

	private Node createResourceRoot(Node technology, String resourceName, String testResourceUri) {
		NodeService nodeService = CorePlugin.getInstance().getNodeService();
		
		// create resource file
		ServiceContext<NodeService> context  = new ServiceContext<NodeService>(nodeService);
		context.getContext().put(NAME, resourceName);
		context.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);
		context.getContext().put(CoreConstants.OVERWRITE_IF_NECESSARY, true);
		Node resourceNode = new Node(null, CoreConstants.FILE_NODE_TYPE);
		nodeService.addChild(technology, resourceNode, context);
		
		// subscribe to resource
		String matchUri = resourceNode.getNodeUri().replaceFirst(FILE_SCHEME, "fpp");
		new ResourceServiceRemote().subscribeToParentResource(matchUri);
		
		CorePlugin.getInstance().getResourceSetService().reload(matchUri, new ServiceContext<ResourceSetService>());
		Node root = CorePlugin.getInstance().getResourceService().getNode(matchUri);
		nodeService.setProperty(root, RESOURCE_URI, testResourceUri, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(root, SHOW_GROUPED_BY_REGEX, false, new ServiceContext<NodeService>(nodeService));
		return root;
	}
	
	/**
	 * @param nodeUri
	 * @return compare results
	 * @throws Exception
	 * @author Elena Posea
	 */
	public String testMatchesForAll(String nodeUri) {
		VirtualNodeResourceHandler virtualNodeHandler = CorePlugin.getInstance().getVirtualNodeResourceHandler();
		String technology = virtualNodeHandler.getTypeSpecificPartFromNodeUri(nodeUri);
		String repo = CoreUtils.getRepoFromNodeUri(nodeUri);
		String compareResult = "";
		
		String testFilesNodeUri = virtualNodeHandler.createVirtualNodeUri(repo, REGEX_TEST_FILES_NODE_TYPE, technology); 
		Node testFilesNode = CorePlugin.getInstance().getResourceService().getNode(testFilesNodeUri);
		List<Node> testFilesNodeChildren = CorePlugin.getInstance().getNodeService().getChildren(testFilesNode, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));

		for (Node testFileNode : testFilesNodeChildren) {
			compareResult += compareForTestFile(testFileNode);
		}
		return compareResult;
	}

	/**
	 * @param nodeUri
	 * @return compare results
	 * @throws Exception
	 * @author Elena Posea
	 */
	public String testMatchesForSelection(String testFileUri) {
		return compareForTestFile(CorePlugin.getInstance().getResourceService().getNode(testFileUri));
	}
	
	private String compareForTestFile(Node testFile) {
		String matches = compareForTestFile(testFile.getNodeUri(), REGEX_MATCHES_NODE_TYPE, REGEX_EXPECTED_MATCHES_NODE_TYPE);
		String model = compareForTestFile(testFile.getNodeUri(), REGEX_MODEL_TREE_NODE_TYPE, REGEX_EXPECTED_MODEL_TREE_NODE_TYPE);
		return ResourcesPlugin.getInstance().getMessage("regex.test.compare.result", testFile.getPropertyValue(NAME), matches, model);
	}

	private String compareForTestFile(String testFileUri, String actual, String expected) {
		String actualNodeUri = testFileUri.replaceFirst(REGEX_TEST_FILE_NODE_TYPE, actual);
		String expectedNodeUri = testFileUri.replaceFirst(REGEX_TEST_FILE_NODE_TYPE, expected);
		
		ResourceServiceRemote rsr = new ResourceServiceRemote(); 
		rsr.subscribeToParentResource(actualNodeUri);
		rsr.subscribeToParentResource(expectedNodeUri);
		
		Node actualNode = CorePlugin.getInstance().getResourceService().getNode(actualNodeUri);
		Node expectedNode = CorePlugin.getInstance().getResourceService().getNode(expectedNodeUri);
		String result = compare(actualNode, expectedNode);
		return result != null ? result : ResourcesPlugin.getInstance().getMessage("regex.test.compare.result.ok");
	}
	
	private String compare(Node actual, Node expected) {
		List<Node> actualChildren = CorePlugin.getInstance().getNodeService().getChildren(actual, 
				new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		List<Node> expectedChildren = CorePlugin.getInstance().getNodeService().getChildren(expected, 
				new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		
		// check number of children
		if (actualChildren.size() != expectedChildren.size()) {
			return ResourcesPlugin.getInstance().getMessage("regex.test.compare.result.error.childrenCount", actual.getPropertyValue(NAME));
		}
		
		for (int i = 0; i < actualChildren.size(); i++) {
			Node actualChild = actualChildren.get(i);
			Node expectedChild = expectedChildren.get(i);
			// compare child properties
			String propertiesCompare = compareProperties(actualChild, expectedChild);
			if (propertiesCompare != null) {
				return propertiesCompare;
			}
			// recurse
			String childrenCompare = compare(actualChild, expectedChild);
			if (childrenCompare != null) {
				return childrenCompare;
			}
		}
		return null;
	}
	
	private String compareProperties(Node actual, Node expected) {
		// check type
		if (!actual.getType().equals(expected.getType())) {
			return ResourcesPlugin.getInstance().getMessage("regex.test.compare.result.eror.type", actual.getPropertyValue(NAME));
		}
		
		// check properties
		ServiceContext<NodeService> context = new ServiceContext<>(CorePlugin.getInstance().getNodeService());
		Map<String, Object> expectedProperties = expected.getOrPopulateProperties(context);
		Map<String, Object> actualProperties = actual.getOrPopulateProperties(context);
		
		// check properties size
		if (actualProperties.size() != expectedProperties.size()) {
			return ResourcesPlugin.getInstance().getMessage("regex.test.compare.result.error.propertiesCount", actual.getPropertyValue(NAME));
		}
		
		// iterate properties
		for (Entry<String, Object> entry : expectedProperties.entrySet()) {
			if (!actualProperties.containsKey(entry.getKey())) {
				return ResourcesPlugin.getInstance().getMessage("regex.test.compare.result.propertyNotFound", 
						entry.getKey(), actual.getPropertyValue(NAME));
			}
			Object expectedPropertyValue = entry.getValue();
			Object actualPropertyValue = actualProperties.get(entry.getKey());
			if (!Utils.safeEquals(actualPropertyValue, expectedPropertyValue)) {
				return ResourcesPlugin.getInstance().getMessage("regex.test.compare.result.error.propertyValue",
						entry.getKey(), actual.getPropertyValue(NAME),
						expectedPropertyValue, actualPropertyValue);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param folder
	 * @return a list of all files in that folder
	 */
	public List<Object> getTestFiles(Object folder) {
		IFileAccessController controller = CorePlugin.getInstance().getFileAccessController();
		Object[] files = controller.listFiles(folder);
		if (files == null) {
			return Collections.emptyList();
		}
		
		List<Object> result = new ArrayList<Object>();
		for (Object file : files) {
			if (controller.isDirectory(file)) {
				// recurse for directories
				result.addAll(getTestFiles(file));
			} else {
				// keep child file
				result.add(file);
			}
		}
		return result;
	}
}
