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
package org.flowerplatform.codesync.remote;

import static org.flowerplatform.codesync.CodeSyncConstants.BASE_DIR;
import static org.flowerplatform.codesync.CodeSyncConstants.NODE_ANCESTOR;
import static org.flowerplatform.codesync.CodeSyncConstants.NODE_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.SRC_DIR;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncAlgorithm.Side;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;
import org.flowerplatform.codesync.config_loader.ICodeSyncConfigLoader;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.controller.TypeDescriptorRemote;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	private List<ICodeSyncConfigLoader> codeSyncConfigLoaders = new ArrayList<ICodeSyncConfigLoader>();

	private Map<String, TypeDescriptorRegistry> codeSyncConfigs = new HashMap<String, TypeDescriptorRegistry>();
	
	public List<ICodeSyncConfigLoader> getCodeSyncConfigLoaders() {
		return codeSyncConfigLoaders;
	}

	/**
	 * Remote method.
	 */
	public void reloadConfiguration(String nodeUri) {
		String codeSyncConfigDirsKey = getCodeSyncConfigDirsKeyFromNodeUri(nodeUri);
		loadCodeSyncConfig(codeSyncConfigDirsKey);
		for (String key : codeSyncConfigs.keySet()) {
			if (!key.equals(codeSyncConfigDirsKey) && key.contains(codeSyncConfigDirsKey)) {
				loadCodeSyncConfig(key);
			}
		}
	}
	
	/**
	 * Remote method.
	 */
	public List<TypeDescriptorRemote> getCodeSyncConfigurationRemote(String nodeUri) {
		String codeSyncConfigDirs = getCodeSyncConfigDirsKeyFromNodeUri(nodeUri);
		TypeDescriptorRegistry registry = getOrLoadCodeSyncConfig(codeSyncConfigDirs);
		return registry.getTypeDescriptorsRemote();
	}
	
	private String getCodeSyncConfigDirsKeyFromNodeUri(String nodeUri) {
		Node config = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		IPath path = getResourceNodeLocation(config);
		return getCodeSyncConfigDirsKey(path.toString(), null);
	}
	
	/**
	 * Return the configuration from the cached map, loading if necessary.
	 */
	public TypeDescriptorRegistry getOrLoadCodeSyncConfig(String codeSyncConfigDirsKey) {
		TypeDescriptorRegistry config = codeSyncConfigs.get(codeSyncConfigDirsKey);
		if (config == null) {
			config = loadCodeSyncConfig(codeSyncConfigDirsKey);
		}
		return config;
	}
	
	private TypeDescriptorRegistry loadCodeSyncConfig(String codeSyncConfigDirs) {
		TypeDescriptorRegistry config = new TypeDescriptorRegistry();
		config.getOrCreateTypeDescriptor(CodeSyncConstants.CODE_SYNC_CONFIG_NOTYPE);
		for (ICodeSyncConfigLoader loader : codeSyncConfigLoaders) {
			loader.load(codeSyncConfigDirs.split(","), config);
		}
		codeSyncConfigs.put(codeSyncConfigDirs, config);
		return config;
	}
	
	/**
	 * Relativize paths to base directory and sort configuration directories paths.
	 * 
	 * @param codeSyncConfigDirs comma-separated string of the paths of the configuration directories;
	 * as written by the user in the root node of the model
	 * @param baseDir base directory that serves as the root for all the configuration directories
	 * @return a comma-separated string of the relativized and sorted paths
	 */
	public String getCodeSyncConfigDirsKey(String codeSyncConfigDirs, String baseDir) {
		List<String> result = null;
		String[] dirs = codeSyncConfigDirs.split(",");
		
		// relativize to base dir
		if (baseDir == null) {
			result = Arrays.asList(dirs);
		} else {
			IPath path = new Path(baseDir);
			result = new ArrayList<String>();
			for (String dir : dirs) {
				result.add(path.append(dir).toString());
			}
		}
		
		// sort
		Collections.sort(result);
		
		return Utils.joinString(",", result);
	}
	
	/**
	 * @author Valentina Bojan
	 **/
	public Match synchronize(String nodeUri) throws Exception {
		Node srcDir = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();

		while (!SRC_DIR.equals(srcDir.getType())) {
			srcDir = CorePlugin.getInstance().getNodeService().getParent(srcDir, context);
		}
		
		Object file = CorePlugin.getInstance().getFileAccessController()
				.getFile(getPath(srcDir, (String) srcDir.getPropertyValue(NAME)));
		String technologies = (String) srcDir.getPropertyValue(CodeSyncConstants.SRC_DIR_TECHNOLOGIES);
		return synchronize(srcDir.getNodeUri(), file, technologies, true);
	}

	/**
	 * Perform synchronization for a source directory. First generate the match, and if 
	 * <code>oneStepSync</code> is <code>true</code>, also apply the synchronization actions.
	 * 
	 * @param nodeUri the node URI of the source directory in the model file
	 * @param file the source directory as a file on disk
	 * @param technology the technology for this source directory
	 * @param oneStepSync a boolean flag; <code>true</code> to also apply sync actions
	 * @return the match tree for the synchronization
	 * 
	 * @see #generateMatch(String, Object, String, boolean)
	 * @see #performSync(Match)
	 */
	public Match synchronize(String nodeUri, Object file, String technology, boolean oneStepSync) {
		Match match = generateMatch(nodeUri, file, technology, oneStepSync);
		if (!oneStepSync) {
			performSync(match);
		}

		return match;
	}

	/**
	 * Perform a 3-way compare (ancestor: initial model, left: current model, right: AST), and generate
	 * the match tree.
	 */
	public Match generateMatch(String nodeUri, Object file, String technology, boolean oneStepSync) {
		Node srcDir = CorePlugin.getInstance().getResourceService().getNode(nodeUri);

		// START THE ALGORITHM

		// STEP 1: create a match
		Match match = new Match();

		// ancestor + left: model (Node structure)
		match.setAncestor(srcDir);
		match.setLeft(srcDir);

		// right: source code (file system)
		Object ast = file;
		match.setRight(new CodeSyncFile(ast, true));

		// initialize the algorithm
		CodeSyncAlgorithm algorithm = new CodeSyncAlgorithm();
		algorithm.initializeModelAdapterSets(
				Collections.singletonList(NODE_LEFT), 
				Collections.singletonList(technology),
				Collections.singletonList(NODE_ANCESTOR));
		algorithm.initializeFeatureProvider(Side.RIGHT);
		algorithm.setFileAccessController(CorePlugin.getInstance().getFileAccessController());
		match.setCodeSyncAlgorithm(algorithm);
		match.setMatchKey(algorithm.getAncestorModelAdapter(srcDir).getMatchKey(srcDir, algorithm));

		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match, oneStepSync);

		return match;
	}

	/**
	 * Apply the default sync actions for this match tree.
	 * 
	 * @author Mariana Gheorghe
	 **/
	public Match performSync(Match match) {
		// STEP 3: sync
		match.getCodeSyncAlgorithm().synchronize(match);
		return match;
	}
	
	/**
	 * @see #getPathFromResourceNode(Node, String)
	 */
	public String getPath(Node node, String relativePath) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
		resourceNode.getOrPopulateProperties(new ServiceContext<NodeService>());
		return getPathFromResourceNode(resourceNode, relativePath);
	}
	
	/**
	 * Find the base directory of this model file, and append the <code>relativePath</code>.
	 * 
	 * @param resourceNode the root of the model file; contains the {@link CodeSyncConstants#BASE_DIR} property
	 * (a path relative to the location of the model file)
	 * @param relativePath relative to the base directory
	 * @return the path (relative to the workspace root), formed from the base directory and relative path
	 */
	public String getPathFromResourceNode(Node resourceNode, String relativePath) {
		IPath path = getResourceNodeLocation(resourceNode);
		String baseDir = (String) resourceNode.getProperties().get(BASE_DIR);
		if (baseDir != null) {
			path = path.append(baseDir);
		}
		if (relativePath != null) {
			path = path.append(relativePath);
		}
		return path.toString();
	}
	
	private IPath getResourceNodeLocation(Node resourceNode) {
		IPath path = new Path(FileControllerUtils.getFilePathWithRepo(resourceNode));
		path = path.removeLastSegments(1);
		return path;
	}

}