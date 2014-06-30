package org.flowerplatform.codesync.regex.remote;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.codesync.regex.CodeSyncRegexConstants;
import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.codesync.regex.action.CodeSyncRegexAction;
import org.flowerplatform.codesync.regex.action.DelegatingRegexWithAction;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexProcessingSession;

public class CodeSyncRegexService {

	private Pattern newLinePattern;
	
	public List<Pair<String, String>> getRegexActions() {	
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		
		for (RegexAction regexAction : CodeSyncRegexPlugin.getInstance().getActions().values()) {
			list.add(new Pair<String, String>(regexAction.getName(), regexAction.getDescription()));
		}		
		return list;
	}
	
	public String generateMatches(String nodeUri) {
		ServiceContext<NodeService> context;	
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(nodeUri);
					
		List<Node> children = CorePlugin.getInstance().getNodeService().getChildren(resourceNode, new ServiceContext<>(CorePlugin.getInstance().getNodeService()));
		List<Node> regexes = new ArrayList<>();		
		for (Node child :children) {
			if (child.getType().equals(CodeSyncRegexConstants.REGEX_TYPE)) {
				regexes.add(child);
			}
		}
		RegexConfiguration regexConfig = new RegexConfiguration();		
		for (Node regex : regexes) {
			regexConfig.add(new DelegatingRegexWithAction().setNode(regex).setRegexAction(new CodeSyncRegexAction.IfFindThisSkip()));
		}
		regexConfig.compile(Pattern.DOTALL);
		
		try {
			final Object data =  FileControllerUtils.getFileAccessController().getContent(FileControllerUtils.getFileAccessController().getFile("user1/repo-1/yyy/jsFile.js"));
			final String content = IOUtils.toString((InputStream) data);
			final RegexProcessingSession session = regexConfig.startSession(content);	
		
			Object file = FileControllerUtils.getFileAccessController().getFile(FileControllerUtils.getFilePathWithRepo(resourceNode));
			Object parentFile = FileControllerUtils.getFileAccessController().getParentFile(file);
			
			String parentNodeUri = FileControllerUtils.createFileNodeUri(FileControllerUtils.getRepo(resourceNode), FileControllerUtils.getFileAccessController().getPath(parentFile));
			Node parent = CorePlugin.getInstance().getResourceService().getNode(parentNodeUri);
			
			Node matchFile = new Node(null, CoreConstants.FILE_NODE_TYPE);			
			context = new ServiceContext<NodeService>();
			context.getContext().put(NAME, FileControllerUtils.getNextAvailableName(FileControllerUtils.getFileAccessController().getPath(parentFile) + "/" + "test.regexMatches"));
			context.getContext().put(CoreConstants.FILE_IS_DIRECTORY, false);
			CorePlugin.getInstance().getNodeService().addChild(parent, matchFile, context);
			
			String matchUri = matchFile.getNodeUri().replace(FILE_SCHEME, "fpp");
			new ResourceServiceRemote().subscribeToParentResource(matchUri);
			final Node matchRoot = CorePlugin.getInstance().getResourceService().getNode(matchUri);
			
			session.find(new Runnable() {
				int currentIndex = 1;			
				
				@Override
				public void run() {					
					Node match = new Node(null, REGEX_MATCH_TYPE);
					
					ServiceContext<NodeService> context = new ServiceContext<NodeService>();
					context.getContext().put("name", session.getCurrentRegex().getName());
					context.getContext().put("regex", ((DelegatingRegexWithAction) session.getCurrentRegex()).getRegexWithMacros());
					context.getContext().put("value", String.valueOf(currentIndex++));
					context.getContext().put("start", formatIndex(content, session.getMatcher().start()));
					context.getContext().put("end", formatIndex(content, session.getMatcher().end()));
					CorePlugin.getInstance().getNodeService().addChild(matchRoot, match, context);
																					
					if (session.getCurrentSubMatchesForCurrentRegex() != null) { // has subMatches -> add them too
						for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
							int index = session.getCurrentMatchGroupIndex() + i + 1;
													
							context = new ServiceContext<NodeService>();
							context.getContext().put("value", session.getCurrentSubMatchesForCurrentRegex()[i]);
							context.getContext().put("start", formatIndex(content, session.getMatcher().start(index)));
							context.getContext().put("end", formatIndex(content, session.getMatcher().end(index)));
							CorePlugin.getInstance().getNodeService().addChild(match, new Node(null, REGEX_MATCH_TYPE), context);
						}
					}
				}
			});
			
			CorePlugin.getInstance().getResourceService().save(matchUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
			return matchUri;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String formatIndex(String str, int index) {		
		String prefix = str.substring(0, index);
		
		if (newLinePattern == null) { // first time
			newLinePattern = Pattern.compile("(\r\n)|(\n)|(\r)");
		}
		
		Matcher m = newLinePattern.matcher(prefix); // search for new line separator
		int lines = 0;
		int lineEndIndex = -1;
		while (m.find()) {			
		    lines++;
		    lineEndIndex = m.end();
		}	
		return "L" + (lines + 1) + " C" + (lineEndIndex == -1 ? index : index - lineEndIndex + 1);
		
	}
	
}
