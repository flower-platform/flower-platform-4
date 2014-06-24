package org.flowerplatform.codesync.sdiff;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_CHILDREN_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_CONFLICT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_LEFT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_DIFFS_MODIFIED_RIGHT;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_FEATURE;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_MODEL_ELEMENT_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_TYPE;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFFS_FOLDER;
import static org.flowerplatform.codesync.sdiff.CodeSyncSdiffConstants.STRUCTURE_DIFF_EXTENSION;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.file.FileControllerUtils.createFileNodeUri;
import static org.flowerplatform.core.file.FileControllerUtils.getFilePathWithRepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.eclipse.compare.internal.core.patch.FilePatch2;
import org.eclipse.compare.internal.core.patch.Hunk;
import org.eclipse.compare.internal.core.patch.PatchReader;
import org.eclipse.compare.patch.IFilePatch2;
import org.eclipse.compare.patch.IFilePatchResult;
import org.eclipse.compare.patch.IHunk;
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
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.resources.ResourcesPlugin;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.file.StringHolder;

/**
 * @author Mariana Gheorghe
 */
@SuppressWarnings("restriction")
public class StructureDiffService {

	/**
	 * 
	 * @param patch
	 * @param repo
	 * @param sdiffPath relative to structure diffs folder
	 * @return
	 */
	public Node createStructureDiff(String patch, String repo, String sdiffPath) {
		boolean reverse = true;
		
		// create file and subscribe to sdiff root
		String sdiffFileUri = createSdiffFile(repo, sdiffPath);
		String sdiffUri = sdiffFileUri.replace(FILE_SCHEME, "fpp");
		new ResourceServiceRemote().subscribeToParentResource(sdiffUri);
		Node sdiffRoot = CorePlugin.getInstance().getResourceService().getNode(sdiffUri);
		CorePlugin.getInstance().getNodeService().setProperty(sdiffRoot, NAME,
				ResourcesPlugin.getInstance().getMessage("codesync.sdiff.structureDiff"),
				new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService()));
		
		// prepare to parse patch
		PatchReader reader = new PatchReader();
		try {
			reader.parse(new BufferedReader(new StringReader(patch)));
		} catch (IOException e) {
			throw new RuntimeException(ResourcesPlugin.getInstance().getMessage("codesync.sdiff.error.cannotParsePatch"), e);
		}
		
		// apply patch per file
		FilePatch2[] filePatches = reader.getDiffs();
		if (filePatches.length == 0) {
			throw new RuntimeException(ResourcesPlugin.getInstance().getMessage("codesync.sdiff.error.noFileDiffs"));
		}
		for (FilePatch2 filePatch : filePatches) {
			// get the current file content
			String filePath = repo + "/" + getFilePath(filePatch.getPath(reverse).toString(), reader.isGitPatch());
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
			Match match = sync(patchedContent, currentContent, filePath.substring(filePath.lastIndexOf("/") + 1));
			addToSdiffFile(sdiffRoot, match, filePatch);
		}
		
		// save the structure diff file
		CorePlugin.getInstance().getResourceService().save(sdiffUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		
		// return the file node
		return CorePlugin.getInstance().getResourceService().getNode(sdiffFileUri);
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
	
	private void addToSdiffFile(Node parent, Match match, FilePatch2 patch) {
		// create child
		Node child = new Node(null, MATCH);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		CorePlugin.getInstance().getNodeService().addChild(parent, child, context);
		
		// populate properties from match
		CorePlugin.getInstance().getNodeService().setProperty(child, NAME, match.getMatchKey(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_TYPE, match.getMatchType().toString(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_FEATURE, match.getFeature() == null ? "" : match.getFeature(), context);
		Object modelElementType = match.getCodeSyncAlgorithm().getTypeProvider().getType(match.getDelegate());
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_MODEL_ELEMENT_TYPE, modelElementType, context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_CHILDREN_MODIFIED_LEFT, match.isChildrenModifiedLeft(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_CHILDREN_MODIFIED_RIGHT, match.isChildrenModifiedRight(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_CHILDREN_CONFLICT, match.isChildrenConflict(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_DIFFS_MODIFIED_LEFT, match.isDiffsModifiedLeft(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_DIFFS_MODIFIED_RIGHT, match.isDiffsModifiedRight(), context);
		CorePlugin.getInstance().getNodeService().setProperty(child, MATCH_DIFFS_CONFLICT, match.isDiffsConflict(), context);
		
		// match to lines from patch
		if (match.getLeft() != null && match.getRight() != null) {
			Object model = match.getRight();
			int modelStartLine = CodeSyncPlugin.getInstance().getLineInformationProvider().getStartLine(model);
			int modelEndLine = CodeSyncPlugin.getInstance().getLineInformationProvider().getEndLine(model);
			if (modelStartLine >= 0 && modelEndLine >= 0) {
				CorePlugin.getInstance().getNodeService().setProperty(child, CodeSyncConstants.MATCH_BODY_MODIFIED, 
						isBodyModified(patch, modelStartLine, modelEndLine), context);
			}
		}
		
		// recurse for submatches
		for (Match subMatch : match.getSubMatches()) {
			addToSdiffFile(child, subMatch, patch);
		}
	}
	
	private boolean isBodyModified(IFilePatch2 patch, int modelStartLine, int modelEndLine) {
		for (IHunk iHunk : patch.getHunks()) {
			Hunk hunk = (Hunk) iHunk;
			int hunkStartLine = hunk.getStart(true);
			int hunkEndLine = hunkStartLine + hunk.getLength(true);
			int overlap = overlap(hunkStartLine, hunkEndLine, modelStartLine, modelEndLine);
			if (overlap == 0) {
				// this hunk overlaps with the model element
				// check if the modified lines appear in the model element
				int index = hunkStartLine;
				for (String line : hunk.getLines()) {
					if (line.startsWith("+")) {
						// added line
						if (overlap(index, index, modelStartLine, modelEndLine) == 0) { 
							return true;
						}
						index++;
					} else if (line.startsWith("-")) {
						// deleted line
						if (overlap(index, index, modelStartLine, modelEndLine) == 0) { 
							return true;
						}
					} else {
						// context line, no change
						index++;
					}
				}
			}
			if (overlap >= 0) {
				// this hunk appears after the model element
				// since the hunk are sorted, there's no need to continue
				break;
			}
		}
		return false;
	}
	
	/**
	 * @return 
	 * <ul>
	 * 	<li> -1, if the left line range appears before the right line range
	 * 	<li> 0, if the ranges overlap
	 * 	<li> 1, if the left line range appears after the right line range
	 * </ul>
	 */
	private int overlap(int startLeft, int endLeft, int startRight, int endRight) {
		if (endLeft < startRight) {
			return -1;
		}
		if (endRight < startLeft) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * Create a new structure diff file in the structure diffs folder
	 * (also create the folder if it does not yet exist).
	 * 
	 * <p>
	 * If a file with that name already exists, the next available name
	 * will be used instead (e.g. trying to create Untitled, when
	 * Untitled, Untitled1 and Untitled2 already exist => Untitled3 
	 * will be created).
	 */
	private String createSdiffFile(String repo, String fileName) {
		// subscribe to file system to avoid errors
		new ResourceServiceRemote().subscribeToParentResource(Utils.getUri(FILE_SCHEME, repo));
		
		// check if structure diffs folder exists
		String sdiffsFolderUri = createFileNodeUri(repo, STRUCTURE_DIFFS_FOLDER);
		Object sdiffsFolder;
		try {
			sdiffsFolder = CorePlugin.getInstance().getFileAccessController().getFile(getFilePathWithRepo(sdiffsFolderUri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (!CorePlugin.getInstance().getFileAccessController().exists(sdiffsFolder)) {
			// create the folder
			createSdiffsFolder(repo);
		}
		
		// create the file
		fileName = getNextAvailableName(repo, fileName);
		Node parent = CorePlugin.getInstance().getResourceService().getNode(sdiffsFolderUri);
		Node child = new Node(null, FILE_NODE_TYPE);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.getContext().put(NAME, fileName + STRUCTURE_DIFF_EXTENSION);
		context.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);
		CorePlugin.getInstance().getNodeService().addChild(parent, child, context);
		return child.getNodeUri();
	}
	
	private void createSdiffsFolder(String repo) {
		Node parent = CorePlugin.getInstance().getResourceService().getNode(createFileNodeUri(repo, null));
		Node child = new Node(null, FILE_NODE_TYPE);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.getContext().put(NAME, STRUCTURE_DIFFS_FOLDER);
		context.getContext().put(CoreConstants.FILE_IS_DIRECTORY, true);
		CorePlugin.getInstance().getNodeService().addChild(parent, child, context);
	}
	
	private String getNextAvailableName(String repo, String fileName) {
		try {
			Object file = CorePlugin.getInstance().getFileAccessController().getFile(
					repo + "/" + STRUCTURE_DIFFS_FOLDER + "/" + fileName + STRUCTURE_DIFF_EXTENSION);
			if (!CorePlugin.getInstance().getFileAccessController().exists(file)) {
				return fileName;
			}
			// first group is name, second group is index
			Pattern pattern = Pattern.compile("(.*?)(\\d*)$");
			Matcher matcher = pattern.matcher(fileName);
			if (matcher.find()) {
				int index = 1;
				if (matcher.group(2).length() > 0) {
					index = Integer.parseInt(matcher.group(2));
				}
				index++;
				return getNextAvailableName(repo, matcher.group(1) + index);
			}
			return getNextAvailableName(repo, fileName + "2");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
