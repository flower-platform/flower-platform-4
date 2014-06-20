package org.flowerplatform.codesync.structured_diff;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_FEATURE;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.eclipse.compare.internal.core.patch.FilePatch2;
import org.eclipse.compare.internal.core.patch.PatchReader;
import org.eclipse.compare.patch.IFilePatchResult;
import org.eclipse.compare.patch.PatchConfiguration;
import org.eclipse.compare.patch.ReaderCreator;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.type_provider.ComposedTypeProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.NodeServiceRemote;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.file.StringHolder;

/**
 * @author Mariana Gheorghe
 */
@SuppressWarnings("restriction")
public class StructureDiffService {

	public Node createStructureDiff(String patch, String sDiffPath) {
		boolean reverse = true;
		
		String repo = sDiffPath.substring(0, sDiffPath.indexOf("|"));
		String sdiffFileUri = createSdiffFile(repo, sDiffPath.substring(sDiffPath.lastIndexOf("/") + 1));
		sdiffFileUri = sdiffFileUri.replace("file", "fpp");
		new ResourceServiceRemote().subscribeToParentResource(sdiffFileUri);
		Node sdiffRoot = CorePlugin.getInstance().getResourceService().getNode(sdiffFileUri);
		
		PatchReader reader = new PatchReader();
		try {
			reader.parse(new BufferedReader(new StringReader(patch)));
		} catch (IOException e) {
			throw new RuntimeException("Cannot parse patch", e);
		}
		
		// apply patch per file
		FilePatch2[] filePatches = reader.getDiffs();
		for (FilePatch2 filePatch : filePatches) {
			// get the current file content
			String filePath = repo + "/" + getFilePath(filePatch.getPath(true).toString(), reader.isGitPatch());
			String currentContent = getFileContent(filePath);
			
			// apply patch
			PatchConfiguration configuration = new PatchConfiguration();
			configuration.setReversed(reverse);
			IFilePatchResult result = filePatch.apply(new StringReaderCreator(currentContent), configuration, null);
			String patchedContent;
			try {
				patchedContent = IOUtils.toString(result.getPatchedContents());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			// start codesync
			Match match = sync(patchedContent, currentContent, filePath.substring(filePath.lastIndexOf("/")));
			addToSdiffFile(sdiffRoot, match);
		}
		
		CorePlugin.getInstance().getResourceService().save(sdiffFileUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		
		String nodeUri = FileControllerUtils.createFileNodeUri(repo, sDiffPath);
		return CorePlugin.getInstance().getResourceService().getNode(nodeUri);
	}
	
	private Match sync(String before, String after, String name) {
		// START THE ALGORITHM
		
		// STEP 1: create a match
		Match match = new Match();
		match.setMatchKey(name);
		
		// ancestor + left: original content obtained after applying reverse patch
		match.setAncestor(new StringHolder(before, name));
		match.setLeft(new StringHolder(before, name));
	
		// right: current content for this patch
		match.setRight(new StringHolder(after, name));
		
		// initialize the algorithm
		ITypeProvider typeProvider = new ComposedTypeProvider()
				.addTypeProvider(CodeSyncPlugin.getInstance().getTypeProvider("java"));
		TypeDescriptorRegistry typeDescriptorRegistry = CorePlugin.getInstance().getNodeTypeDescriptorRegistry();
		
		CodeSyncAlgorithm algorithm = new CodeSyncAlgorithm(typeDescriptorRegistry, typeProvider);
		match.setCodeSyncAlgorithm(algorithm);
		
		// STEP 2: generate the diff, i.e. 3-way compare
		algorithm.generateDiff(match, false);
		
		return match;
	}
	
	private void addToSdiffFile(Node parent, Match match) {
		// create child
		Node child = new Node(null, MATCH);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		CorePlugin.getInstance().getNodeService().addChild(parent, child, context);
		
		// populate properties from match
		CorePlugin.getInstance().getNodeService().setProperty(child, NAME, match.getMatchKey(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_TYPE, match.getMatchType(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_FEATURE, match.getFeature() == null ? "" : match.getFeature(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_CHILDREN_MODIFIED_LEFT, match.isChildrenModifiedLeft(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_CHILDREN_MODIFIED_RIGHT, match.isChildrenModifiedRight(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_CHILDREN_CONFLICT, match.isChildrenConflict(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_DIFFS_MODIFIED_LEFT, match.isDiffsModifiedLeft(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_DIFFS_MODIFIED_RIGHT, match.isDiffsModifiedRight(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_DIFFS_CONFLICT, match.isDiffsConflict(), context);
		
		// recurse for submatches
		for (Match subMatch : match.getSubMatches()) {
			addToSdiffFile(child, subMatch);
		}
	}
	
	private String createSdiffFile(String repo, String fileName) {
		Node parent = CorePlugin.getInstance().getResourceService().getNode(
				FileControllerUtils.createFileNodeUri(repo, "sdiffs"));
		Node child = new Node(null, FILE_NODE_TYPE);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.getContext().put(NAME, fileName);
		context.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);
		CorePlugin.getInstance().getNodeService().addChild(parent, child, context);
		return child.getNodeUri();
	}
	
	private String getFilePath(String path, boolean isGitPatch) {
		if (isGitPatch) {
			return path.substring(path.indexOf("/") + 1);
		}
		return path;
	}
	
	/**
	 * Return the content of the file, if the file exists. Otherwise, return the empty string.
	 */
	private String getFileContent(String path) {
		Object file;
		try {
			file = CorePlugin.getInstance().getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (CorePlugin.getInstance().getFileAccessController().exists(file)) {
			return CorePlugin.getInstance().getFileAccessController().readFileToString(file);
		}
		return "";
	}
	
	class StringReaderCreator extends ReaderCreator {

		private String content;
		
		public StringReaderCreator(String content) {
			this.content = content;
		}
		
		@Override
		public Reader createReader() {
			return new StringReader(content);
		}
	}
	
}
