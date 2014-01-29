/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
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
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync.code;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.ModelAdapterFactorySet;
import org.flowerplatform.core.mindmap.remote.Node;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 */
public class CodeSyncCodePlugin extends AbstractFlowerJavaPlugin {
	
	public static String MARKER_ANNOTATION = "_MARKER";
	
	public static String SINGLE_MEMBER_ANNOTATION = "_SINGLE_MEMBER";
	 
	public static String NORMAL_ANNOTATION = "_NORMAL";
	
	public final static String SINGLE_MEMBER_ANNOTATION_VALUE_NAME = "_";
	
	protected static CodeSyncCodePlugin INSTANCE;
	
	private final static Logger logger = LoggerFactory.getLogger(CodeSyncCodePlugin.class);

	public static CodeSyncCodePlugin getInstance() {
		return INSTANCE;
	}
	
	protected ModelAdapterFactorySetProvider modelAdapterFactorySetProvider;
	
	public ModelAdapterFactorySetProvider getModelAdapterFactorySetProvider() {
		return modelAdapterFactorySetProvider;
	}

	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		initializeExtensionPoint_modelAdapterFactorySet();
	}
	
	private void initializeExtensionPoint_modelAdapterFactorySet() throws CoreException {
		modelAdapterFactorySetProvider = new ModelAdapterFactorySetProvider();
		IConfigurationElement[] configurationElements = Platform.getExtensionRegistry().getConfigurationElementsFor("org.flowerplatform.codesync.code.modelAdapterFactorySet");
		for (IConfigurationElement configurationElement : configurationElements) {
			String id = configurationElement.getAttribute("id");
			String technology = configurationElement.getAttribute("technology");
			Object instance = configurationElement.createExecutableExtension("modelAdapterFactorySetClass");
			modelAdapterFactorySetProvider.getFactorySets().put(technology, (ModelAdapterFactorySet) instance);
			logger.debug("Added CodeSync ModelAdapterFactorySet with id = {} with class = {}", id, instance.getClass());
		}
	}

	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
		INSTANCE = null;
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}
	
	public Node getNode(File project, File file, String technology, boolean showDialog) {
		// find model files
		Node root = CodeSyncPlugin.getInstance().getCodeSyncMappingRoot(project);
		
		// find containing SrcDir
		Node srcDir = null;
		File srcDirFile = null;
		File parent = file;
		do {
			srcDir = CodeSyncPlugin.getInstance().getSrcDir(root, parent.getName());
			srcDirFile = parent;
			parent = parent.getParentFile();
		} while (srcDir == null && !parent.equals(project));
		if (srcDir == null) {
			throw new RuntimeException("File " + file + " is not contained in a SrcDir!");
		}
		
		// find the CodeSyncElement in the SrcDir
		String relativeToSrcDir = getPathRelativeToFile(file, srcDirFile);
		// there are cases when path format is a\b\c and the split method will not return correctly.
		// so replace \ with /
		relativeToSrcDir = relativeToSrcDir.replaceAll("\\\\", "/");
		if (relativeToSrcDir.startsWith("/")) {
			relativeToSrcDir = relativeToSrcDir.substring(1);
		}
		String[] fragments = relativeToSrcDir.length() > 0 ? relativeToSrcDir.split("/") : new String[0];
		Node codeSyncElement = getCodeSyncElement(srcDir, fragments);
		
		String srcDirPath = getPathRelativeToFile(srcDirFile, project);
		String relativeToProject = getPathRelativeToFile(file, project);
		if (codeSyncElement == null || showDialog) {
			runCodeSyncAlgorithm(srcDir, project, srcDirPath, relativeToProject, technology, showDialog);
		} else {
			if (showDialog) {
				runCodeSyncAlgorithm(srcDir, project, srcDirPath, relativeToProject, technology, showDialog);
			}
			return codeSyncElement;
		}
		return getCodeSyncElement(srcDir, fragments);
	}
	
	/**
	 * Finds the {@link Node} corresponding to the <code>path</code> by traversing the model tree
	 * rooted at the <code>srcDir</code>.
	 */
	public Node getCodeSyncElement(Node srcDir, String[] path) {
		Node node = srcDir;
		boolean foundForPath = true;
		for (int i = 0; i < path.length; i++) {
			boolean foundChild = false;
			List<Node> children = (List<Node>) CodeSyncPlugin.getInstance().getMindMapService()
					.getChildrenForNodeId(node.getId()).get(1);
			for (Node child : children) {
				String name = child.getBody();
				if (name.equals(path[i])) {
					node = child;
					foundChild = true;
					break;
				}
			}
			if (!foundChild) {
				foundForPath = false;
				break;
			}
		}
		if (foundForPath) {
			return node;
		}
		return null;
	}
	
	/**
	 * @see CodeSyncAlgorithm#generateDiff(Match)
	 * @see CodeSyncAlgorithm#synchronize(Match)
	 */
	public Match runCodeSyncAlgorithm(Node model, File project, String path, String limitedPath, String technology, /*CommunicationChannel communicationChannel,*/ boolean showDialog) {
		// STEP 1: create a match
		Match match = new Match();
		
		// ancestor + left: model (Node structure)
		match.setAncestor(model);
		match.setLeft(model);
	
		// right: source code (file system)
		Object ast = CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, path);
		match.setRight(ast);
		
		// get the adapter set for the technology
		ModelAdapterFactorySet modelAdapterFactorySet = getModelAdapterFactorySetProvider().getFactorySets().get(technology);
		modelAdapterFactorySet.initialize(limitedPath, CodeSyncPlugin.getInstance().useUIDs());
		match.setModelAdapterFactorySet(modelAdapterFactorySet);
		
		CodeSyncAlgorithm algorithm =  new CodeSyncAlgorithm(modelAdapterFactorySet);
		
		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match);
		
		// STEP 3: sync
		algorithm.synchronize(match);
		
//		if (!showDialog) {
//			// we're not showing the dialog => perform sync
//			StatefulServiceInvocationContext context = new StatefulServiceInvocationContext(communicationChannel);
//			service.synchronize(context, editableResourcePath);
//			// and unsubscribe
//			service.unsubscribeAllClientsForcefully(editableResourcePath, false);
//		}
		
		return match;
	}
	
	public String getPathRelativeToFile(File file, File relativeTo) {
		String relative = relativeTo.toURI().relativize(file.toURI()).getPath();
		if (relative.length() > 0 && relative.endsWith("/")) {
			relative = relative.substring(0, relative.length() - 1);
		}
		return relative;
	}
	
}
