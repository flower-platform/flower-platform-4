/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_MATCH_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_NAME;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.REGEX_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.RESOURCE_URI;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.SHOW_GROUPED_BY_REGEX;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START_C;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.START_L;
import static org.flowerplatform.core.CoreConstants.FILE_IS_DIRECTORY;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.OVERRIDE;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.codesync.regex.action.CodeSyncRegexAction;
import org.flowerplatform.codesync.regex.action.DelegatingRegexWithAction;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.file.FileControllerUtils;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ResourceServiceRemote;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.Utils;
import org.flowerplatform.util.regex.RegexAction;
import org.flowerplatform.util.regex.RegexConfiguration;
import org.flowerplatform.util.regex.RegexProcessingSession;

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
	public String generateMatches(String nodeUri, String textNodeUri, String newPath, boolean override) throws Exception {
		final NodeService nodeService = CorePlugin.getInstance().getNodeService();
		ServiceContext<NodeService> context;	
		IFileAccessController fileController = FileControllerUtils.getFileAccessController();
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(nodeUri);
		
		// get text file & content
		Object textFile = fileController.getFile(FileControllerUtils.getFilePathWithRepo(textNodeUri));		
		final String textFileContent = IOUtils.toString((InputStream) fileController.getContent(textFile));

		Object fileToGenerate = FileControllerUtils.getFileAccessController().getFile(newPath);
		Object parentFile = fileController.getParentFile(fileToGenerate);
		
		if (!fileController.exists(parentFile)) {
			fileController.createFile(parentFile, true);
		}
		String parentFilePath = fileController.getPath(parentFile);		
		String parentNodeUri = FileControllerUtils.createFileNodeUri(CoreUtils.getRepoFromNode(resourceNode), parentFilePath);
		
		Node parent = CorePlugin.getInstance().getResourceService().getNode(parentNodeUri);
		
		String name = null;
		if (FileControllerUtils.getFileAccessController().exists(fileToGenerate) && !override) {
			name = FileControllerUtils.getNextAvailableName(newPath);			
		} else {
			name = fileController.getName(fileToGenerate);
		}
		
		String matchUri;
		if (name != null) {
			// create matches file
			Node matchFile = new Node(null, FILE_NODE_TYPE);			
			context = new ServiceContext<NodeService>(nodeService);
			context.getContext().put(NAME, name);
			context.getContext().put(FILE_IS_DIRECTORY, false);
			context.getContext().put(OVERRIDE, override);
			nodeService.addChild(parent, matchFile, context);
			
			matchUri = matchFile.getNodeUri();
		} else {
			matchUri = FileControllerUtils.createFileNodeUri(Utils.getRepo(nodeUri), newPath);
		}
		
		matchUri = matchUri.replace(FILE_SCHEME, "fpp");
		// TODO CC: unsubscribe resource if needed (to discuss with MG)
		
		// subscribe file using fpp schema	
		new ResourceServiceRemote().subscribeToParentResource(matchUri);
		
		// get matches root node & save the textNodeUri as property
		final Node matchRoot = CorePlugin.getInstance().getResourceService().getNode(matchUri);
		nodeService.setProperty(matchRoot, RESOURCE_URI, textNodeUri, new ServiceContext<NodeService>(nodeService));
		nodeService.setProperty(matchRoot, SHOW_GROUPED_BY_REGEX, false, new ServiceContext<NodeService>(nodeService));
				
		// create regEx configuration
		RegexConfiguration regexConfig = new RegexConfiguration();		
		for (Node regex : CodeSyncRegexPlugin.getInstance().getChildren(resourceNode, REGEX_TYPE)) {
			regexConfig.add(new DelegatingRegexWithAction().setNode(regex).setRegexAction(new CodeSyncRegexAction.IfFindThisSkip()));
		}
		regexConfig.compile(Pattern.DOTALL);
		
		// start session
		final RegexProcessingSession session = regexConfig.startSession(textFileContent);
				
		// find matches
		session.find(new Runnable() {
			int currentIndex = 1;			
			
			@Override
			public void run() {	
				// match found => create node
				Node match = new Node(null, REGEX_MATCH_TYPE);
				DelegatingRegexWithAction regex = (DelegatingRegexWithAction) session.getCurrentRegex();
				
				ServiceContext<NodeService> context = new ServiceContext<NodeService>();
				context.getContext().put(NAME, String.format("%s) %s", String.valueOf(currentIndex++), session.getCurrentRegex().getName()));
				context.getContext().put(FULL_REGEX, regex.getRegex());
				context.getContext().put(REGEX_NAME, regex.getName());
				
				int[] start = formatIndex(textFileContent, session.getMatcher().start());
				context.getContext().put(START, String.format("L%d C%d", start[0], start[1]));
				context.getContext().put(START_L, start[0]);
				context.getContext().put(START_C, start[1]);
				
				int[] end = formatIndex(textFileContent, session.getMatcher().end());
				context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
				context.getContext().put(END_L, end[0]);
				context.getContext().put(END_C, end[1]);
				
				nodeService.addChild(matchRoot, match, context);
																				
				if (session.getCurrentSubMatchesForCurrentRegex() != null) { // has subMatches -> add them too as children
					for (int i = 0; i < session.getCurrentSubMatchesForCurrentRegex().length; i++) {
						int index = session.getCurrentMatchGroupIndex() + i + 1;
												
						context = new ServiceContext<NodeService>();
						context.getContext().put(NAME, session.getCurrentSubMatchesForCurrentRegex()[i]);
						
						start = formatIndex(textFileContent, session.getMatcher().start(index));
						context.getContext().put(START, String.format("L%d C%d", start[0], start[1]));
						context.getContext().put(START_L, start[0]);
						context.getContext().put(START_C, start[1]);
						
						end = formatIndex(textFileContent, session.getMatcher().end(index));
						context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
						context.getContext().put(END_L, end[0]);
						context.getContext().put(END_C, end[1]);
						
						context.getContext().put(END, String.format("L%d C%d", end[0], end[1]));
						nodeService.addChild(match, new Node(null, REGEX_MATCH_TYPE), context);
					}
				}
			}
		});
			
		// everything worked ok => save file
		CorePlugin.getInstance().getResourceService().save(matchUri, new ServiceContext<ResourceService>(CorePlugin.getInstance().getResourceService()));
		
		// matchUri is returned to be open on client
		return matchUri;		
	}
	
	private int[] formatIndex(String str, int index) {		
		String prefix = str.substring(0, index);
		
		Matcher m = newLinePattern.matcher(prefix); // search for new line separator
		int lines = 0;
		int lineEndIndex = -1;
		while (m.find()) {			
		    lines++;
		    lineEndIndex = m.end();
		}	
		return new int[] {lines, (lineEndIndex == -1 ? index : index - lineEndIndex)};		
	}		
	
}