package org.flowerplatform.codesync.structured_diff;

import static org.flowerplatform.codesync.CodeSyncConstants.MATCH;
import static org.flowerplatform.codesync.CodeSyncConstants.MATCH_ID_SEPARATOR;
import static org.flowerplatform.codesync.structured_diff.CodeSyncSdiffConstants.STRUCTURE_DIFF;
import static org.flowerplatform.core.file.FileControllerUtils.getRepo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.compare.internal.core.patch.FilePatch2;
import org.eclipse.compare.internal.core.patch.PatchReader;
import org.eclipse.compare.patch.IFilePatchResult;
import org.eclipse.compare.patch.PatchConfiguration;
import org.eclipse.compare.patch.ReaderCreator;
import org.eclipse.core.runtime.CoreException;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.type_provider.ComposedTypeProvider;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.resource.IResourceHandler;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;
import org.flowerplatform.util.file.StringHolder;

/**
 * @author Mariana Gheorghe
 */
@SuppressWarnings("restriction")
public class StructureDiffResourceHandler implements IResourceHandler {

	@Override
	public String getResourceUri(String nodeUri) {
		return Utils.getUri(STRUCTURE_DIFF, getRepo(nodeUri), null);
	}

	@Override
	public Object getRawNodeDataFromResource(String nodeUri, Object resourceData) {
		String fragment = Utils.getFragment(nodeUri);
		if (fragment == null) {
			// structured diff node
			return resourceData;
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Match> matches = (Map<String, Match>) resourceData;
		int index = fragment.indexOf(MATCH_ID_SEPARATOR);
		if (index < 0) {
			// root match
			return matches.get(fragment);
		}
		Match match = matches.get(fragment.substring(0, index));
		fragment = fragment.substring(index + 1);
		return getMatch(fragment, match);
	}

	private Match getMatch(String uri, Match match) {
		int index = uri.indexOf(MATCH_ID_SEPARATOR);
		String segment = index < 0 ? uri : uri.substring(0, index);
		for (Match subMatch : match.getSubMatches()) {
			if (segment.equals(subMatch.getMatchKey())) {
				if (index < 0) {
					// last segment
					return subMatch;
				} else {
					return getMatch(uri.substring(index + 1), subMatch);
				}
			}
		}
		throw new RuntimeException("Submatch not found for uri: " + uri);
	}
	
	@Override
	public Node createNodeFromRawNodeData(String nodeUri, Object rawNodeData) {
		String type = MATCH;
		if (Utils.getFragment(nodeUri) == null) {
			type = STRUCTURE_DIFF;
		}
		Node node = new Node(nodeUri, type);
		node.setRawNodeData(rawNodeData);
		return node;
	}

	@Override
	public Object load(String resourceUri) throws Exception {
		String repo = getRepo(resourceUri);
		
		PatchReader reader = new PatchReader();
		reader.parse(new BufferedReader(new FileReader(
				CorePlugin.getInstance().getFileAccessController().getAbsolutePath(
						CorePlugin.getInstance().getFileAccessController().getFile(repo + "/test/diff")))));
		
		FilePatch2[] diffs = reader.getDiffs();
		
		Map<String, Match> matches = new HashMap<String, Match>();
		
		for (FilePatch2 diff : diffs) {
			String name = diff.getPath(true).lastSegment();
			ReaderCreator content = new FileReaderCreator(CorePlugin.getInstance().getFileAccessController().getAbsolutePath(
					CorePlugin.getInstance().getFileAccessController().getFile(repo + "/test/" + name)));
			PatchConfiguration configuration = new PatchConfiguration();
			configuration.setReversed(true);
			IFilePatchResult result = diff.apply(content, configuration, null);
			String patchedContent = IOUtils.toString(result.getPatchedContents());
		
			// start codesync
			String currentContent = IOUtils.toString(new FileInputStream(CorePlugin.getInstance().getFileAccessController().getAbsolutePath(
					CorePlugin.getInstance().getFileAccessController().getFile(repo + "/test/" + name))));
			Match match = sync(patchedContent, currentContent, name);
			
			matches.put(name, match);
		}
		
		return matches;
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
	
	@Override
	public void save(Object resourceData) throws Exception {
		// nothing to do
	}

	@Override
	public boolean isDirty(Object resourceData) {
		return false;
	}

	@Override
	public void unload(Object resourceData) throws Exception {
		// nothing to do
	}
	
	class FileReaderCreator extends ReaderCreator {

		private String path;
		
		public FileReaderCreator(String path) {
			this.path = path;
		}
		
		@Override
		public Reader createReader() throws CoreException {
			try {
				return new FileReader(path);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
